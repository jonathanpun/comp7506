package cs.hku.comp7506.util

import android.util.Log
import android.util.Log.DEBUG


fun debug(string:String){
    Log.d("debug_tag", string)
}

fun debug(o:Any){
    debug(o.toString())
}