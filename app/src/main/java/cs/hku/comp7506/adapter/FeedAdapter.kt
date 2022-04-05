package cs.hku.comp7506.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cs.hku.comp7506.databinding.ViewholderFeedItemBinding
import cs.hku.comp7506.model.Feed

class FeedAdapter:ListAdapter<FeedDisplayModel,RecyclerView.ViewHolder>(FeedDiffUtil()) {
    companion object {
        const val VIEW_TYPE_FEED=0
        const val VIEW_TYPE_LOADING= 1
    }

    override fun getItemViewType(position: Int): Int {
        return when(currentList[position]){
            is FeedDisplayModel.FeedDisplay-> VIEW_TYPE_FEED
            is FeedDisplayModel.LoadingDisplay-> VIEW_TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ViewholderFeedItemBinding.inflate(LayoutInflater.from(parent.context))
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = currentList[position]
        when(holder){
            is FeedViewHolder->(data as? FeedDisplayModel.FeedDisplay)?.let { holder.bind(it) }
        }
    }

    class FeedViewHolder(val binding:ViewholderFeedItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data:FeedDisplayModel.FeedDisplay){
            
        }
    }

    class FeedDiffUtil:DiffUtil.ItemCallback<FeedDisplayModel>(){
        override fun areItemsTheSame(oldItem: FeedDisplayModel, newItem: FeedDisplayModel): Boolean {
            return (oldItem as? FeedDisplayModel.FeedDisplay)?.id == (newItem as? FeedDisplayModel.FeedDisplay)?.id
        }

        override fun areContentsTheSame(oldItem: FeedDisplayModel, newItem: FeedDisplayModel): Boolean {
            return (oldItem as? FeedDisplayModel.FeedDisplay)?.id == (newItem as? FeedDisplayModel.FeedDisplay)?.id
        }

    }
}
