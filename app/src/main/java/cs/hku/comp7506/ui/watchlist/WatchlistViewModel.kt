package cs.hku.comp7506.ui.watchlist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class WatchlistViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(listOf("hi"))
    val uiState:Flow<List<String>> = _uiState

}