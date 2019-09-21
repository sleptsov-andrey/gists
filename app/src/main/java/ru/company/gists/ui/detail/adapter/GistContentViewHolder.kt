package ru.company.gists.ui.detail.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import ru.company.gists.R
import ru.company.gists.data.network.entity.File

class GistContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var tvName: TextView
    private lateinit var tvContent: TextView

    companion object {
        private val LAYOUT = R.layout.item_content

        fun create(parent: ViewGroup): GistContentViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
            return GistContentViewHolder(view)
        }
    }

    init {
        findViews(itemView)
    }

    fun bindItem(file: File) {
        tvName.text = file.filename
        tvContent.text = file.content
    }

    private fun findViews(itemView: View) {
        with(itemView) {
            tvName = findViewById(R.id.tv_file_name)
            tvContent = findViewById(R.id.tv_file_content)
        }
    }

}
