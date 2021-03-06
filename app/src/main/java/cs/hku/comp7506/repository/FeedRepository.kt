package cs.hku.comp7506.repository

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.type.LatLng
import cs.hku.comp7506.model.Feed
import cs.hku.comp7506.model.Poi
import cs.hku.comp7506.model.PoiNNResponse
import cs.hku.comp7506.model.converter.FeedConverter
import cs.hku.comp7506.model.converter.PoiConverter
import cs.hku.comp7506.util.debug
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.google.firebase.storage.FirebaseStorage
import cs.hku.comp7506.model.CreatePostModel
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.*


class FeedRepository(private val contentResolver: ContentResolver) {
    val db = FirebaseFirestore.getInstance()
    val fn = FirebaseFunctions.getInstance();
    val storage: FirebaseStorage = FirebaseStorage.getInstance()
    var lastDoc: DocumentSnapshot? = null

    init {
        //fn.useEmulator("10.0.2.2", 5001);
    }

    suspend fun getFeed(refresh: Boolean, poiId: String?): List<Feed>? {
        return getFeedDocument(refresh, poiId).mapNotNull {
            if (it == null)
                return null
            val poi = it.getString("poi_id")?.let { getPoi(it) }
            FeedConverter.fromSnapShot(it, poi = poi)
        }
    }

    suspend fun getFeedDocument(refresh: Boolean, poiId: String?): List<DocumentSnapshot?> =
        suspendCoroutine { cont ->
            db.collection("feed").let {
                if (poiId != null)
                    it.whereEqualTo("poi_id", poiId)
                else
                    it
            }.apply {
                val lastDoc = lastDoc
                if (!refresh && lastDoc != null)
                    startAfter(lastDoc)
            }
                .orderBy("time", Query.Direction.DESCENDING)

                .limit(10)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        lastDoc = document.documents.lastOrNull()
                        cont.resume(document.documents)
                    }
                }.addOnFailureListener {
                    it.printStackTrace()
                }
        }

    suspend fun getPoi(id: String): Poi? = suspendCoroutine { cont ->
        db.collection("poi").document(id)
            .get().addOnSuccessListener {
                cont.resume(PoiConverter.fromSnapshot(it))
            }
    }

    suspend fun searchPoi(query: String): List<Poi> = suspendCoroutine { cont ->
        db.collection("poi").orderBy("name").startAt(query).endAt("$query~").get()
            .addOnSuccessListener {
                cont.resume(it.documents.mapNotNull { PoiConverter.fromSnapshot(it) })
            }
    }

    suspend fun getNearestPoi(lat: Double, lng: Double): PoiNNResponse? = suspendCoroutine { cont ->
        fn.getHttpsCallable("nearestpoi").call(
            mapOf(
                "lat" to "22.373957736600694",
                "lon" to "114.17719798312167"
            )
        ).continueWith {
            val response = Gson().fromJson(it.result.data.toString(), PoiNNResponse::class.java)
            cont.resume(response)
        }
    }

    suspend fun createPost(image:List<Uri>,content:String,poi:PoiNNResponse) {
        val imageUrls= image.map { uploadImage(it).toString() }
        val data = CreatePostModel(content=content, images = imageUrls, poiId = poi.id)
        suspendCoroutine<Boolean> { cont->
            db.collection("feed").add(data).addOnSuccessListener {
                cont.resume(true)
            }
        }
    }


    private suspend fun uploadImage(uri: Uri)= suspendCoroutine<Uri> { cont->
        val type = contentResolver.getType(uri)
        val stream = contentResolver.openInputStream(uri)
        val storageRef = storage.reference
        val riversRef = storageRef.child("images/${UUID.randomUUID()}")
        val uploadTask = riversRef.putStream(stream!!)
        uploadTask.continueWithTask {
            stream.close()
            riversRef.downloadUrl
        }.addOnSuccessListener {
            debug(it)
            cont.resume(it)
        }
    }
}
