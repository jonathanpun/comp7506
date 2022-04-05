package cs.hku.comp7506.adapter

sealed class FeedDisplayModel {
    data class FeedDisplay(
        val id:String,
        val timeString:String,
        val contentString:String
    ):FeedDisplayModel()
    object LoadingDisplay:FeedDisplayModel()
}