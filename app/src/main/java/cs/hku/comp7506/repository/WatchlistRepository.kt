package cs.hku.comp7506.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import cs.hku.comp7506.model.KeyWatch
import cs.hku.comp7506.model.Poi
import cs.hku.comp7506.model.converter.PoiConverter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class WatchlistRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth
    suspend fun getPoi(id: String): Poi? = suspendCoroutine { cont ->
        db.collection("poi").document(id)
            .get().addOnSuccessListener {
                cont.resume(PoiConverter.fromSnapshot(it))
            }
    }

    suspend fun getWatchList():List<KeyWatch>{

        val docs = suspendCoroutine<List<DocumentSnapshot>> {cont->
            db.collection("user/${auth.currentUser?.uid}/watchlist").limit(10).get().addOnSuccessListener {
                    cont.resume(it.documents)
            }.addOnFailureListener {
                it.printStackTrace()
            }
        }
       return docs.mapNotNull {
            when(it.getString("type")){
                "poi"->{
                    getPoi(it.getString("poi_id")?:"")?.let{
                        KeyWatch.PoiWatch(it)
                    }
                }
                else->null
            }
        }

    }
}