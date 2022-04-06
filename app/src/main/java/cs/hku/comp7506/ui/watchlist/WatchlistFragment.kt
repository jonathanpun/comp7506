package cs.hku.comp7506.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cs.hku.comp7506.databinding.FragmentWatchlistBinding
import kotlinx.coroutines.flow.collectLatest

class WatchlistFragment:Fragment() {
    val vm:WatchlistViewModel by viewModels()
    private var _composeView:ComposeView? = null
    private val composeView:ComposeView
    get() = _composeView!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _composeView = ComposeView(requireContext())
        return composeView.apply { 
            setContent { 
                buildList()
            }
        }
    }

    @Preview
    @Composable
    fun buildList(vm:WatchlistViewModel= viewModel()){
        val l = vm.uiState.collectAsState(initial = emptyList())
        Column(modifier= Modifier.width(IntrinsicSize.Max)) {
            l.value.forEach { watchlist ->
                Box(Modifier.fillMaxWidth()){

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _composeView= null
    }
}