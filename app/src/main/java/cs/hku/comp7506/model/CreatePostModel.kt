package cs.hku.comp7506.model

import android.net.Uri
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import com.google.gson.annotations.SerializedName
import java.util.*

 data  class CreatePostModel (
    @ServerTimestamp
    val time: Date? = null ,
    val content:String,
    @get:PropertyName("poi_id")
    val poiId:String,
    val images:List<String>
    )