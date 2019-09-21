package ru.company.gists.ui.detail.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import ru.company.gists.R
import ru.company.gists.data.network.entity.Commit

class CommitsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var tvVersion: TextView
    private lateinit var tvCommitedAt: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvAdditions: TextView
    private lateinit var tvDeletions: TextView

    companion object {

        private val LAYOUT = R.layout.item_commit

        fun create(parent: ViewGroup): CommitsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
            return CommitsViewHolder(view)
        }
    }

    init {
        findViews(itemView)
    }


    fun bindItem(commit: Commit) {
        tvVersion.text = itemView.context.getString(R.string.version, commit.version)
        tvCommitedAt.text = itemView.context.getString(R.string.commited, commit.committedAt)
        tvTotal.text = itemView.context.getString(R.string.total, commit.changeStatus.total)
        tvAdditions.text = itemView.context.getString(R.string.additions, commit.changeStatus.additions)
        tvDeletions.text = itemView.context.getString(R.string.deletions, commit.changeStatus.deletions)
    }

    private fun findViews(itemView: View) {
        with(itemView) {
            tvVersion = findViewById(R.id.tv_version)
            tvCommitedAt = findViewById(R.id.tv_commited_at)
            tvTotal = findViewById(R.id.tv_total)
            tvAdditions = findViewById(R.id.tv_additions)
            tvDeletions = findViewById(R.id.tv_deletions)
        }
    }

}
