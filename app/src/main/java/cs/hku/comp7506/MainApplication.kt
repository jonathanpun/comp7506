package cs.hku.comp7506

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase

class MainApplication : Application(){
    init {
        FirebaseApp.initializeApp(this)
    }
}