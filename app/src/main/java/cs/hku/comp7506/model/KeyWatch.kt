package cs.hku.comp7506.model

sealed class KeyWatch {
    data class PoiWatch(val id: String, val poi: Poi) : KeyWatch()
}