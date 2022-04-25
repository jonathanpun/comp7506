package cs.hku.comp7506.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cs.hku.comp7506.R
import cs.hku.comp7506.repository.LoginRepository
import cs.hku.comp7506.util.NavDirection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class EntryViewModel(val loginRepository: LoginRepository):ViewModel() {
    private val _animatorFlow = MutableStateFlow(false)
    private val _signInFlow = MutableStateFlow(false)
    val nav: Flow<NavDirection?> = _animatorFlow.combine(_signInFlow) { a, b ->
        if (a && b)
            NavDirection.Direction(R.id.action_entryFragment_to_navigation_home)
        else
            null
    }

    fun init() {
        viewModelScope.launch {
            loginRepository.signIn()
            _signInFlow.value = true
        }
    }

    fun animatorEnded() {
        _animatorFlow.value = true
    }
}