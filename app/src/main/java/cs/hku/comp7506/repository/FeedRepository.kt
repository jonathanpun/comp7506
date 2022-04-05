package cs.hku.comp7506.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import cs.hku.comp7506.model.Feed
import cs.hku.comp7506.model.Poi
import cs.hku.comp7506.model.converter.FeedConverter
import cs.hku.comp7506.model.converter.PoiConverter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FeedRepository {
    val db = FirebaseFirestore.getInstance()

    suspend fun getFeed(): List<Feed>? {
        val document = getFeedDocument()
        val poi = document.getString("poi_id")?.let { getPoi(it) }
        return FeedConverter.fromSnapShot(document, poi =poi)?.let { listOf(it) }
    }

    suspend fun getFeedDocument(): DocumentSnapshot = suspendCoroutine { cont ->
        db.collection("feed").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    cont.resume(document.first())
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }}


        suspend fun getPoi(id: String): Poi? = suspendCoroutine { cont ->
                db.collection("poi").document(id)
                    .get().addOnSuccessListener {
                         cont.resume(PoiConverter.fromSnapshot(it) )
                    }
        }

    }
