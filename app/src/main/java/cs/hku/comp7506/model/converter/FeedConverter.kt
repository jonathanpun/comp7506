package cs.hku.comp7506.model.converter

import com.google.firebase.firestore.DocumentSnapshot
import cs.hku.comp7506.model.Feed
import cs.hku.comp7506.model.Poi
import cs.hku.comp7506.util.DateUtil
import java.lang.Exception

object FeedConverter {
    fun fromSnapShot(snapshot: DocumentSnapshot,poi: Poi?): Feed?{
        return try {
            Feed( id = snapshot.id,date =  snapshot.getTimestamp("time")?.seconds?.let { DateUtil.getDateTime(it) }.orEmpty() ,content= snapshot.getString("content").orEmpty(),
                images = snapshot.get("images") as List<String>,
                geoPoint =  snapshot.getGeoPoint("geo_point"), poi = poi)
        }catch (e:Exception){
            e.printStackTrace()
            return null
        }
    }
}