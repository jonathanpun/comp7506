package cs.hku.comp7506.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cs.hku.comp7506.repository.LoginRepository

class EntryViewModelFactory:ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EntryViewModel(LoginRepository()) as T
    }
}