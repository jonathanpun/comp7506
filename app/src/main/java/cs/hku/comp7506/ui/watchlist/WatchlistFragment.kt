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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cs.hku.comp7506.databinding.FragmentWatchlistBinding
import cs.hku.comp7506.model.KeyWatch
import cs.hku.comp7506.repository.WatchlistRepository
import kotlinx.coroutines.flow.collectLatest

class WatchlistFragment : Fragment() {
    val vm: WatchlistViewModel by viewModels(factoryProducer = {
        object :ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return WatchlistViewModel(WatchlistRepository()) as T
            }
        }
    })
    private var _composeView: ComposeView? = null
    private val composeView: ComposeView
        get() = _composeView!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _composeView = ComposeView(requireContext())
        return composeView.apply {
            setContent {
                buildList(vm)
            }
        }
    }

    @Composable
    fun buildList(vm: WatchlistViewModel) {
        val l = vm.uiState.collectAsState(initial = emptyList())
        Column(modifier = Modifier.width(IntrinsicSize.Max)) {
            Box(
                Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(color = Color(0xFF015396))
            ) {
                Text(
                    "WatchList", modifier = Modifier
                        .padding(start = 24.dp, bottom = 24.dp)
                        .align(Alignment.BottomStart),
                    color = Color.White, fontSize = 36.sp
                )
            }
            l.value.forEach { watchlist ->
                when(watchlist){
                    is KeyWatch.PoiWatch->buildPoiWatch(watchlist)
                }
            }
        }
    }

    @Composable
    fun buildPoiWatch(poi:KeyWatch.PoiWatch){
        Box(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        )
        {
            Text(
                text = poi.poi.name,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Box(
                modifier = Modifier
                    .background(color = Color.Black)
                    .align(
                        Alignment.BottomCenter
                    )
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _composeView = null
    }
}