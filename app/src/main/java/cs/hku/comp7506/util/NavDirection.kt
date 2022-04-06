package cs.hku.comp7506.util

sealed class NavDirection {
    object Back:NavDirection()
    data class Direction(val id:Int):NavDirection()
}