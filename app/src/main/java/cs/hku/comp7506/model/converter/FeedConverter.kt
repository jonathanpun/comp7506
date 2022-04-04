package cs.hku.comp7506.model.converter

import com.google.firebase.firestore.DocumentSnapshot
import cs.hku.comp7506.model.Feed
import java.lang.Exception

object FeedConverter {
    fun fromSnapShot(snapshot: DocumentSnapshot): Feed?{
        return try {
            Feed( id = snapshot.id,date = "",content= "", images = arrayOf())
        }catch (e:Exception){
            return null
        }
    }
}