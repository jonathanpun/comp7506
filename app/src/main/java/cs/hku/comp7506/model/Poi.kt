package cs.hku.comp7506.model

import com.google.firebase.firestore.GeoPoint
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Poi(
    val id: String,
    @SerializedName("geo_point")
    val geoPoint: GeoPoint,
    val name: String
) : Serializable