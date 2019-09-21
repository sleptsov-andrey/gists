package ru.company.gists.ui.detail.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ru.company.gists.data.network.entity.Commit

class CommitsAdapter : RecyclerView.Adapter<CommitsViewHolder>() {

    private val items = mutableListOf<Commit>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CommitsViewHolder =
            CommitsViewHolder.create(viewGroup)

    override fun onBindViewHolder(commitsViewHolder: CommitsViewHolder, position: Int) {
        val commit = items[position]
        commitsViewHolder.bindItem(commit)
    }

    fun replaceItems(items: List<Commit>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size
}
