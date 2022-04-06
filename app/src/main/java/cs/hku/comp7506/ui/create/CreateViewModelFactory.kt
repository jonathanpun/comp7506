package cs.hku.comp7506.ui.create

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cs.hku.comp7506.repository.FeedRepository

class CreateViewModelFactory(private val contentResolver: ContentResolver):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateViewModel(FeedRepository(contentResolver)) as T
    }
}