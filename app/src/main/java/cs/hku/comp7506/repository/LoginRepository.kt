package cs.hku.comp7506.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun signIn() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            suspendCoroutine<Unit> {
                auth.signInAnonymously()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            db.collection("user").document("${auth.currentUser?.uid}")
                            val user = auth.currentUser
                            it.resume(Unit)
                        } else {

                        }
                    }
            }
        }
        suspendCoroutine<Unit> { cont ->
            FirebaseMessaging.getInstance().token.addOnSuccessListener { fcmID ->
                db.collection("user").document("${auth.currentUser?.uid}").get()
                    .addOnSuccessListener {
                        if (it.exists()) {
                            db.collection("user").document("${auth.currentUser?.uid}")
                                .update("fcm_token", fcmID).addOnSuccessListener {
                                cont.resume(Unit)
                            }
                        } else {
                            db.collection("user").document("${auth.currentUser?.uid}")
                                .set(mapOf("fcm_token" to fcmID)).addOnSuccessListener {
                                cont.resume(Unit)
                            }
                        }
                    }

            }
        }
    }
}