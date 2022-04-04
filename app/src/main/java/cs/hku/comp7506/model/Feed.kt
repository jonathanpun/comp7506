package cs.hku.comp7506.model

data class Feed(
    val id:String,
    val date:String,
    val content:String,
    val images:Array<String>
)
