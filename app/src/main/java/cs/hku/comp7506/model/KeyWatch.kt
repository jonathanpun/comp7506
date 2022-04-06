package cs.hku.comp7506.model

sealed class KeyWatch {
    data class PoiWatch(val poi:Poi):KeyWatch()
}