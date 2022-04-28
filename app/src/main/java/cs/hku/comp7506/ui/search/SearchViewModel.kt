package cs.hku.comp7506.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import cs.hku.comp7506.model.Poi
import cs.hku.comp7506.repository.FeedRepository
import cs.hku.comp7506.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.transform
import kotlin.time.Duration

class SearchViewModel(
    private val feedRepository: FeedRepository,
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {
    private val _searchQueryFlow = MutableStateFlow<String?>(null)
    val poiFlow = _searchQueryFlow.debounce(timeoutMillis = 2000).transform<String?, List<Poi>> {
        if (it != null && it.isNotEmpty()) {
            Log.d("poi debug", it)
            emit(feedRepository.searchPoi(it))
        } else emit(emptyList())
    }

    fun setQuery(query: String) {
        _searchQueryFlow.value = query
    }

    suspend fun clickPoi(poi: Poi) {
        watchlistRepository.addToWatchList(poi)
    }

}