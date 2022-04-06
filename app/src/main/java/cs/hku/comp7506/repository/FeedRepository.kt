package cs.hku.comp7506.repository

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
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
    val storage:FirebaseStorage = FirebaseStorage.getInstance()

    init {
        //fn.useEmulator("10.0.2.2", 5001);
    }
    suspend fun getFeed(): List<Feed>? {
        val document = getFeedDocument()
        val poi = document.getString("poi_id")?.let { getPoi(it) }
        return FeedConverter.fromSnapShot(document, poi = poi)?.let { listOf(it) }
    }

    suspend fun getFeedDocument(): DocumentSnapshot = suspendCoroutine { cont ->
        db.collection("feed").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    cont.resume(document.first())
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

    suspend fun getNearestPoi(lat:Double,lng: Double):PoiNNResponse? = suspendCoroutine { cont->
        fn.getHttpsCallable("nearestpoi").call(
            mapOf(
                "lat" to lat,
                "lon" to lng
            )
        ).continueWith{
            val response= Gson().fromJson(it.result.data.toString(),PoiNNResponse::class.java)
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
