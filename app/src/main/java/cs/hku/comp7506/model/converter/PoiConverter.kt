package cs.hku.comp7506.model.converter

import com.google.firebase.firestore.DocumentSnapshot
import cs.hku.comp7506.model.Poi
import java.lang.Exception

object PoiConverter {
    fun fromSnapshot(snapshot: DocumentSnapshot): Poi? {
        return try {
            Poi(geoPoint = snapshot.getGeoPoint("geo_point")!!, name = snapshot.getString("name")!!)
        }catch (e:Exception){
            null
        }
    }
}