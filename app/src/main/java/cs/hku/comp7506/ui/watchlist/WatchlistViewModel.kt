package cs.hku.comp7506.ui.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cs.hku.comp7506.model.KeyWatch
import cs.hku.comp7506.repository.WatchlistRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WatchlistViewModel(val watchlistRepository: WatchlistRepository): ViewModel() {
    private val _uiState: MutableStateFlow<List<KeyWatch>> = MutableStateFlow(emptyList())
    val uiState: Flow<List<KeyWatch>> = _uiState
    var currentJob: Job? = null

    init {
        viewModelScope.launch {
            _uiState.emit(watchlistRepository.getWatchList())
        }
    }

    fun refreshList() {
        if (currentJob != null)
            return
        currentJob = viewModelScope.launch {
            _uiState.emit(watchlistRepository.getWatchList())
            currentJob = null
        }
    }

    fun removeWatchList(poi: KeyWatch.PoiWatch) {
        viewModelScope.launch {
            watchlistRepository.removeWatchList(poi)
            refreshList()
        }
    }
}