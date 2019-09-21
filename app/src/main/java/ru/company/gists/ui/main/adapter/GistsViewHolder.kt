package ru.company.gists.ui.main.adapter

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import ru.company.gists.R
import ru.company.gists.data.network.entity.Gist
import ru.company.gists.ui.detail.DetailActivity

class GistsViewHolder(itemView: View, val imageLoader: RequestManager) : RecyclerView.ViewHolder(itemView) {
    private lateinit var ivAvatar: ImageView
    private lateinit var tvDescription: TextView
    private lateinit var tvName: TextView
    private lateinit var tvDate: TextView
    private lateinit var progressBar: ProgressBar

    init {
        findViews(itemView)
    }

    companion object {

        private val LAYOUT = R.layout.item_gist

        fun create(parent: ViewGroup, glideRequestManager: RequestManager): GistsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
            return GistsViewHolder(view, glideRequestManager)
        }
    }

    fun bindItem(gist: Gist) {

        progressBar.visibility = View.VISIBLE

        imageLoader.load(gist.owner.avatarUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?,
                                              model: Any,
                                              target: Target<Drawable>,
                                              isFirstResource: Boolean): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable,
                                                 model: Any,
                                                 target: Target<Drawable>,
                                                 dataSource: DataSource,
                                                 isFirstResource: Boolean): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                })
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(0.3f)
                .into(ivAvatar)

        val entry = gist.files.entries.iterator().next()

        tvDescription.text = gist.description
        tvName.text = entry.value.filename
        tvDate.text = gist.updatedAt

        itemView.setOnClickListener { v ->
            val intent = DetailActivity.newIntent(itemView.context, gist.id)
            itemView.context.startActivity(intent)
        }

    }

    private fun findViews(itemView: View) {
        with(itemView) {
            ivAvatar = findViewById(R.id.iv_avatar)
            progressBar = findViewById(R.id.progress_bar)
            tvDescription = findViewById(R.id.tv_gist_description)
            tvName = findViewById(R.id.tv_gist_name)
            tvDate = findViewById(R.id.tv_gist_date)
        }
    }
}
