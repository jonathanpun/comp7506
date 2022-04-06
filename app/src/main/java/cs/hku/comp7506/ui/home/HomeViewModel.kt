package cs.hku.comp7506.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cs.hku.comp7506.model.Feed
import cs.hku.comp7506.repository.FeedRepository
import cs.hku.comp7506.util.LoadState
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HomeViewModel(val feedRepository: FeedRepository, val type: String = "main") : ViewModel() {

    private val _feed = MutableLiveData<LoadState<List<Feed>>>()
    val feed: LiveData<LoadState<List<Feed>>> = _feed
    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title
    private var loadFeedJob: Job? = null

    init {
        loadMoreFeed()
        _title.value = when (type) {
            "main" -> "#Latest feed"
            else -> ""
        }
    }


    fun loadMoreFeed(){
        if (loadFeedJob !=null)
            return
        loadFeedJob = viewModelScope.launch {
            _feed.value = LoadState.Loading(_feed.value?.data?: emptyList())
            val feed = feedRepository.getFeed()
            _feed.value = LoadState.Success(_feed.value?.data?.toMutableList()?.apply {
                feed?.let { addAll(it) }
            } as List<Feed>)
            loadFeedJob = null
        }
    }
}