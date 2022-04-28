package cs.hku.comp7506.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cs.hku.comp7506.R
import cs.hku.comp7506.model.KeyWatch
import cs.hku.comp7506.repository.WatchlistRepository

class WatchlistFragment : Fragment() {
    val vm: WatchlistViewModel by viewModels(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return WatchlistViewModel(WatchlistRepository()) as T
            }
        }
    })
    private var _composeView: ComposeView? = null
    private val composeView: ComposeView
        get() = _composeView!!


    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm.refreshList()
        _composeView = ComposeView(requireContext())
        return composeView.apply {
            setContent {
                buildContent(vm)
            }
        }
    }

    @ExperimentalMaterialApi
    @Composable
    fun buildContent(vm: WatchlistViewModel) {
        Box() {
            buildList(vm)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp, vertical = 100.dp)
            ) {
                FloatingActionButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    backgroundColor = Color(0xFF5D92C4),
                    onClick = {
                        findNavController().navigate(
                            R.id.action_navigation_watchlist_to_searchFragment
                        )
                    }) {
                    Icon(Icons.Filled.Add, "")
                }
            }
        }
    }

    @Composable
    fun buildList(vm: WatchlistViewModel) {
        val l = vm.uiState.collectAsState(initial = emptyList())
        Column(modifier = Modifier.fillMaxWidth()) {
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
                when (watchlist) {
                    is KeyWatch.PoiWatch -> buildPoiWatch(watchlist)
                }
            }
            if (l.value.isEmpty()) {
                Text(text = "You do not have anything in watchlist")
            }
        }

    }

    @Composable
    fun buildPoiWatch(poi: KeyWatch.PoiWatch) {
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
            Icon(Icons.Default.Clear, "",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable {
                        vm.removeWatchList(poi)
                    })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _composeView = null
    }
}