package ru.company.gists.ui.detail.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ru.company.gists.data.network.entity.File

class GistContentAdapter : RecyclerView.Adapter<GistContentViewHolder>() {

    private val items = mutableListOf<File>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): GistContentViewHolder =
         GistContentViewHolder.create(viewGroup)


    override fun onBindViewHolder(gistContentViewHolder: GistContentViewHolder, position: Int) {
        val file = items[position]
        gistContentViewHolder.bindItem(file)
    }

    fun replaceItems(items: List<File>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

}
