package cs.hku.comp7506.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cs.hku.comp7506.R
import cs.hku.comp7506.repository.LoginRepository
import cs.hku.comp7506.util.NavDirection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EntryViewModel(val loginRepository: LoginRepository):ViewModel() {
    private val _nav:MutableStateFlow<NavDirection?> = MutableStateFlow(null)
    val nav:Flow<NavDirection?> = _nav
    fun init(){
        viewModelScope.launch {
            loginRepository.signIn()
            _nav.emit(NavDirection.Direction(R.id.action_entryFragment_to_navigation_home))
        }
    }
}