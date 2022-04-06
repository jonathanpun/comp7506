package cs.hku.comp7506.model

import com.google.firebase.firestore.GeoPoint
import com.google.gson.annotations.SerializedName

data class Poi (
    @SerializedName("geo_point")
    val geoPoint: GeoPoint,
    val name:String
    )