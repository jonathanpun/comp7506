package cs.hku.comp7506.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cs.hku.comp7506.databinding.ViewholderFeedItemBinding
import cs.hku.comp7506.model.Feed

class FeedAdapter:ListAdapter<Feed,RecyclerView.ViewHolder>(FeedDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ViewholderFeedItemBinding.inflate(LayoutInflater.from(parent.context))
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    class FeedViewHolder(val binding:ViewholderFeedItemBinding):RecyclerView.ViewHolder(binding.root)

    class FeedDiffUtil:DiffUtil.ItemCallback<Feed>(){
        override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return oldItem == newItem
        }

    }
}
