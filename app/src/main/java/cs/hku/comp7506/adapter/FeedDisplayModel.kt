package cs.hku.comp7506.adapter

import cs.hku.comp7506.model.Feed

sealed class FeedDisplayModel {
    data class FeedDisplay(
        val id:String,
        val timeString:String,
        val contentString:String,
        val poiString:String?,
        val feed: Feed
    ):FeedDisplayModel()
    object LoadingDisplay:FeedDisplayModel()
}