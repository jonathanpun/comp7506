package cs.hku.comp7506.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import cs.hku.comp7506.model.Feed
import cs.hku.comp7506.model.converter.FeedConverter
import kotlin.coroutines.suspendCoroutine

class FeedRepository {
    val db = FirebaseFirestore.getInstance()

    suspend fun getFeed(): List<Feed>? = suspendCoroutine {
        db.collection("feed").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                     FeedConverter.fromSnapShot(document.first())
                } else {
                   null
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }
    }

}