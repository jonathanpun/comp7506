package cs.hku.comp7506.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cs.hku.comp7506.model.Feed
import cs.hku.comp7506.repository.FeedRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HomeViewModel(val feedRepository: FeedRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            _feed.value= feedRepository.getFeed()
        }
    }

    private val _feed = MutableLiveData<List<Feed>>()
    val feed: LiveData<List<Feed>> = _feed
}