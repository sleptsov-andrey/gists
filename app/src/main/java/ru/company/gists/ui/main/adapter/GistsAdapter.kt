package ru.company.gists.ui.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import ru.company.gists.data.network.entity.Gist

class GistsAdapter(val glideRequestManager: RequestManager) : RecyclerView.Adapter<GistsViewHolder>() {
    private val items = mutableListOf<Gist>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): GistsViewHolder
        = GistsViewHolder.create(viewGroup, glideRequestManager)


    override fun onBindViewHolder(gistsViewHolder: GistsViewHolder, position: Int) {
        val gist = items[position]
        gistsViewHolder.bindItem(gist)
    }

    fun replaceItems(items: List<Gist>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

}
