package cs.hku.comp7506.util

sealed class LoadState<T> (val data:T){
    class Loading<T>(data:T):LoadState<T>(data)
    class Success<T>(data:T):LoadState<T>(data)
}