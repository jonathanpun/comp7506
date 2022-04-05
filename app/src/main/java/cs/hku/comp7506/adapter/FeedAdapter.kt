package cs.hku.comp7506.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cs.hku.comp7506.databinding.ViewholderFeedItemBinding
import cs.hku.comp7506.databinding.ViewholderLoadingBinding
import cs.hku.comp7506.model.Feed
import java.lang.Exception

class FeedAdapter:ListAdapter<FeedDisplayModel,RecyclerView.ViewHolder>(FeedDiffUtil()) {
    companion object {
        const val VIEW_TYPE_FEED=0
        const val VIEW_TYPE_LOADING= 1
    }

    var onPlaceClicked:((feedDisplayModel:FeedDisplayModel.FeedDisplay)->Unit)?  = null
    var onPoiClicked:((feedDisplayModel:FeedDisplayModel.FeedDisplay)->Unit)?  = null

    override fun getItemViewType(position: Int): Int {
        return when(currentList[position]){
            is FeedDisplayModel.FeedDisplay-> VIEW_TYPE_FEED
            is FeedDisplayModel.LoadingDisplay-> VIEW_TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_FEED-> FeedViewHolder( ViewholderFeedItemBinding.inflate(LayoutInflater.from(parent.context)))
            VIEW_TYPE_LOADING->LoadingViewHolder(ViewholderLoadingBinding.inflate(LayoutInflater.from(parent.context)))
            else->throw Exception()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = currentList[position]
        when(holder){
            is FeedViewHolder->(data as? FeedDisplayModel.FeedDisplay)?.let { feedDisplay->
                holder.binding.textviewTime.text = data.timeString
                holder.binding.textviewContent.text = data.contentString
                if (data.poiString!=null){
                    holder.binding.textviewPoi.text = data.poiString
                    holder.binding.imageviewPlace.setOnClickListener{onPoiClicked?.invoke(feedDisplay) }
                    holder.binding.textviewPoi.setOnClickListener{onPoiClicked?.invoke(feedDisplay) }
                }else{
                    holder.binding.imageviewPlace.setOnClickListener { onPlaceClicked?.invoke(feedDisplay) }
                }

            }
        }
    }

    class FeedViewHolder(val binding:ViewholderFeedItemBinding):RecyclerView.ViewHolder(binding.root)
    class LoadingViewHolder(val binding:ViewholderLoadingBinding):RecyclerView.ViewHolder(binding.root)

    class FeedDiffUtil:DiffUtil.ItemCallback<FeedDisplayModel>(){
        override fun areItemsTheSame(oldItem: FeedDisplayModel, newItem: FeedDisplayModel): Boolean {
            return (oldItem as? FeedDisplayModel.FeedDisplay)?.id == (newItem as? FeedDisplayModel.FeedDisplay)?.id
        }

        override fun areContentsTheSame(oldItem: FeedDisplayModel, newItem: FeedDisplayModel): Boolean {
            return (oldItem as? FeedDisplayModel.FeedDisplay)?.id == (newItem as? FeedDisplayModel.FeedDisplay)?.id
        }
    }
}
