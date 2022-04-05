package cs.hku.comp7506.model

import com.google.firebase.firestore.GeoPoint

data class Feed(
    val id:String,
    val date:String,
    val content:String,
    val geoPoint:GeoPoint?,
    val poi:Poi?,
    val images:List<String>
)
