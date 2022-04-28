package cs.hku.comp7506.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cs.hku.comp7506.repository.FeedRepository
import cs.hku.comp7506.repository.WatchlistRepository

class SearchViewModelFactory(
    val feedRepository: FeedRepository,
    val watchlistRepository: WatchlistRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(feedRepository, watchlistRepository) as T
    }
}