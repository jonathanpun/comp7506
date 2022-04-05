package cs.hku.comp7506.adapter

import cs.hku.comp7506.model.Feed
import cs.hku.comp7506.util.LoadState


fun LoadState<List<Feed>>.convertToFeedDisplayModel():List<FeedDisplayModel>{
    val list:MutableList<FeedDisplayModel>  = this.data.map { it.toFeedDisplayModel() }.toMutableList()
    when(this){
        is LoadState.Success->{
            //
        }
        is LoadState.Loading->{
            list.add(FeedDisplayModel.LoadingDisplay)
        }
    }
    return list
}

fun Feed.toFeedDisplayModel():FeedDisplayModel.FeedDisplay =
    FeedDisplayModel.FeedDisplay(id= this.id,
        timeString = this.date,
    contentString = this.content)