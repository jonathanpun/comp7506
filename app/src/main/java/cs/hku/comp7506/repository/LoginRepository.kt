package cs.hku.comp7506.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginRepository {
    private val auth: FirebaseAuth = Firebase.auth

    suspend fun signIn(){
        val currentUser = auth.currentUser
        if (currentUser!=null)
            return
        suspendCoroutine<Unit>{
            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        it.resume(Unit)
                    } else {

                    }
                }
        }
    }
}