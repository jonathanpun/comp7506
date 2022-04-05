package cs.hku.comp7506.model

import com.google.firebase.firestore.GeoPoint

data class Poi (
    val geoPoint: GeoPoint,
    val name:String
    )