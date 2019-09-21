package ru.company.gists.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import ru.company.gists.R
import ru.company.gists.data.network.RestApi
import ru.company.gists.data.network.entity.Gist
import ru.company.gists.ui.State
import ru.company.gists.ui.main.adapter.GistsAdapter
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val LAYOUT = R.layout.activity_main

    private lateinit var toolbar: Toolbar
    private lateinit var rvGists: RecyclerView
    private lateinit var gistsAdapter: GistsAdapter
    private lateinit var btnTryAgain: Button
    private lateinit var viewError: View
    private lateinit var viewLoading: View
    private lateinit var viewNoData: View
    private lateinit var tvError: TextView

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LAYOUT)
        setupUi()
    }

    override fun onStart() {
        super.onStart()
        setupUx()
        loadGists()
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

    private fun onClickTryAgain() = loadGists()

    private fun loadGists() {
        showState(State.Loading)
        val loadDisposable = RestApi.instance
                .gists()
                .getPublicGists()
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

    private fun checkResponseAndShowState(response: Response<List<Gist>>) {

        if (!response.isSuccessful) {
            showState(State.ServerError)
            return
        }

        val data = response.body()
        data ?: run {
            showState(State.HasNoData)
            return
        }

        if (data.isEmpty()) {
            showState(State.HasNoData)
            return
        }

        gistsAdapter.replaceItems(data)
        showState(State.HasData)
    }

    private fun showState(state: State) {
        when (state) {
            State.HasData -> {
                viewError.visibility = View.GONE
                viewLoading.visibility = View.GONE
                viewNoData.visibility = View.GONE

                rvGists.visibility = View.VISIBLE
            }

            State.HasNoData -> {
                rvGists.visibility = View.GONE
                viewLoading.visibility = View.GONE

                viewError.visibility = View.VISIBLE
                viewNoData.visibility = View.VISIBLE
            }
            State.NetworkError -> {
                rvGists.visibility = View.GONE
                viewLoading.visibility = View.GONE
                viewNoData.visibility = View.GONE

                tvError.text = getText(R.string.error_network)
                viewError.visibility = View.VISIBLE
            }

            State.ServerError -> {
                rvGists.visibility = View.GONE
                viewLoading.visibility = View.GONE
                viewNoData.visibility = View.GONE

                tvError.text = getText(R.string.error_server)
                viewError.visibility = View.VISIBLE
            }
            State.Loading -> {
                viewError.visibility = View.GONE
                rvGists.visibility = View.GONE
                viewNoData.visibility = View.GONE

                viewLoading.visibility = View.VISIBLE
            }

        }
    }

    private fun setupRecyclerViews() {
        gistsAdapter = GistsAdapter(Glide.with(this))
        rvGists.layoutManager = LinearLayoutManager(this)
        rvGists.adapter = gistsAdapter

        val mDividerItemDecoration = DividerItemDecoration(rvGists.context,
                DividerItemDecoration.VERTICAL)
        rvGists.addItemDecoration(mDividerItemDecoration)
    }

    private fun findViews() {
        rvGists = findViewById(R.id.rv_gists)
        toolbar = findViewById(R.id.toolbar)
        btnTryAgain = findViewById(R.id.btn_try_again)
        viewError = findViewById(R.id.lt_error)
        viewLoading = findViewById(R.id.lt_loading)
        viewNoData = findViewById(R.id.lt_no_data)
        tvError = findViewById(R.id.tv_error)
    }
}
