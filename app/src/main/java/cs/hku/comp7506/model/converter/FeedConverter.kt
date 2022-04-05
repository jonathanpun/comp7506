package cs.hku.comp7506.model.converter

import com.google.firebase.firestore.DocumentSnapshot
import cs.hku.comp7506.model.Feed
import cs.hku.comp7506.model.Poi
import java.lang.Exception

object FeedConverter {
    fun fromSnapShot(snapshot: DocumentSnapshot,poi: Poi?): Feed?{
        return try {
            Feed( id = snapshot.id,date = "",content= "", images = arrayOf(), geoPoint =  snapshot.getGeoPoint("geo_point"), poi = poi)
        }catch (e:Exception){
            return null
        }
    }
}