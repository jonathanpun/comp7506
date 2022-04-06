package cs.hku.comp7506.ui.home

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cs.hku.comp7506.repository.FeedRepository

class HomeViewModelFactory(private val contentResolver: ContentResolver):ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(FeedRepository(contentResolver)) as T
    }
}