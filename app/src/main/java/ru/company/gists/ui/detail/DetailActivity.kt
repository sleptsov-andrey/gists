package ru.company.gists.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import java.io.IOException
import java.util.ArrayList

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import ru.company.gists.R
import ru.company.gists.data.network.RestApi
import ru.company.gists.data.network.entity.Gist
import ru.company.gists.ui.State
import ru.company.gists.ui.detail.adapter.CommitsAdapter
import ru.company.gists.ui.detail.adapter.GistContentAdapter

class DetailActivity : AppCompatActivity() {

    private val LAYOUT = R.layout.activity_detail

    private lateinit var toolbar: Toolbar
    private lateinit var rvCommits: RecyclerView
    private lateinit var rvGistContent: RecyclerView
    private lateinit var gistContentAdapter: GistContentAdapter
    private lateinit var commitsAdapter: CommitsAdapter
    private lateinit var ivAvatar: ImageView
    private lateinit var btnTryAgain: Button
    private lateinit var viewError: View
    private lateinit var viewLoading: View
    private lateinit var viewNoData: View
    private lateinit var tvError: TextView
    private lateinit var tvUserName: TextView
    private lateinit var gistId: String

    private val compositeDisposable = CompositeDisposable()

    companion object {
        private const val EXTRA_GIST_ID = "ru.company.gists.extra_gist_id"

        fun newIntent(packageContext: Context, gistId: String): Intent {
            val intent = Intent(packageContext, DetailActivity::class.java)
            intent.putExtra(EXTRA_GIST_ID, gistId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LAYOUT)
        setupUi()
    }

    override fun onStart() {
        super.onStart()
        setupUx()
        gistId = intent.getStringExtra(EXTRA_GIST_ID)
        loadGist(gistId)
    }

    override fun onPause() {
        super.onPause()
        unbindUx()
        compositeDisposable.clear()
    }

    private fun unbindUx() = btnTryAgain.setOnClickListener(null)

    private fun setupUi() {
        findViews()
        toolbar.setTitle(R.string.toolbar_title)
        setupRecyclerViews()
    }

    private fun setupUx() = btnTryAgain.setOnClickListener { v -> onClickTryAgain() }

    private fun onClickTryAgain() = loadGist(gistId)

    private fun loadGist(gistId: String?) {
        showState(State.Loading)
        val loadDisposable = RestApi.instance
                .gists()
                .getSingleGist(gistId!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::checkResponseAndShowState, this::handleError)
        compositeDisposable.add(loadDisposable)
    }

    private fun handleError(throwable: Throwable) {
        if (throwable is IOException) {
            showState(State.NetworkError)
            return
        }
        showState(State.ServerError)
    }

    private fun checkResponseAndShowState(response: Response<Gist>) {

        if (!response.isSuccessful) {
            showState(State.ServerError)
            return
        }

        val data = response.body()
        data ?: run {
            showState(State.HasNoData)
            return
        }

        commitsAdapter.replaceItems(data.history)
        gistContentAdapter.replaceItems(ArrayList(data.files.values))
        tvUserName.text = data.owner.login
        Glide.with(this).load(data.owner.avatarUrl).apply(RequestOptions.circleCropTransform()).into(ivAvatar)
        showState(State.HasData)
    }

    private fun showState(state: State) {
        when (state) {
            State.HasData -> {
                viewError.visibility = View.GONE
                viewLoading.visibility = View.GONE
                viewNoData.visibility = View.GONE

                rvGistContent.visibility = View.VISIBLE
                rvCommits.visibility = View.VISIBLE
                tvUserName.visibility = View.VISIBLE
                ivAvatar.visibility = View.VISIBLE
            }

            State.HasNoData -> {
                rvCommits.visibility = View.GONE
                viewLoading.visibility = View.GONE

                viewError.visibility = View.VISIBLE
                viewNoData.visibility = View.VISIBLE
            }
            State.NetworkError -> {
                rvCommits.visibility = View.GONE
                viewLoading.visibility = View.GONE
                viewNoData.visibility = View.GONE

                tvError.text = getText(R.string.error_network)
                viewError.visibility = View.VISIBLE
            }

            State.ServerError -> {
                rvCommits.visibility = View.GONE
                viewLoading.visibility = View.GONE
                viewNoData.visibility = View.GONE

                tvError.text = getText(R.string.error_server)
                viewError.visibility = View.VISIBLE
            }
            State.Loading -> {
                viewError.visibility = View.GONE
                rvCommits.visibility = View.GONE
                viewNoData.visibility = View.GONE

                viewLoading.visibility = View.VISIBLE
            }
        }
    }

    private fun setupRecyclerViews() {
        val mDividerItemDecoration = DividerItemDecoration(rvCommits.context,
                DividerItemDecoration.VERTICAL)

        commitsAdapter = CommitsAdapter()
        rvCommits.layoutManager = LinearLayoutManager(this)
        rvCommits.adapter = commitsAdapter
        rvCommits.addItemDecoration(mDividerItemDecoration)

        gistContentAdapter = GistContentAdapter()
        rvGistContent.layoutManager = LinearLayoutManager(this)
        rvGistContent.adapter = gistContentAdapter
    }

    private fun findViews() {
        rvCommits = findViewById(R.id.rv_commits)
        rvGistContent = findViewById(R.id.rv_gist_content)
        toolbar = findViewById(R.id.toolbar)
        btnTryAgain = findViewById(R.id.btn_try_again)
        viewError = findViewById(R.id.lt_error)
        viewLoading = findViewById(R.id.lt_loading)
        viewNoData = findViewById(R.id.lt_no_data)
        tvError = findViewById(R.id.tv_error)
        tvUserName = findViewById(R.id.tv_gist_name)
        ivAvatar = findViewById(R.id.iv_avatar)
    }
}
