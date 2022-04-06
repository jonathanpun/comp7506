package cs.hku.comp7506.model

import com.google.gson.annotations.SerializedName

data class PoiNNResponse (val id:String,
                          val poi: Poi,
                          @SerializedName("dist")
                          val dist:Double)