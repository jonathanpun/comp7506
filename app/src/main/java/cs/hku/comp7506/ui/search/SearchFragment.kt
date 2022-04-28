package cs.hku.comp7506.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import cs.hku.comp7506.repository.FeedRepository
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.sp
import androidx.lifecycle.coroutineScope
import cs.hku.comp7506.repository.WatchlistRepository

class SearchFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                buildContent()
            }
        }
    }

    @Composable
    fun buildContent(
        vm: SearchViewModel = viewModel(
            factory = SearchViewModelFactory(
                FeedRepository(requireContext().contentResolver),
                WatchlistRepository()
            )
        )
    ) {
        var query by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            TextField(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(4.dp))
                .focusRequester(focusRequester),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                value = query,
                onValueChange = {
                    query = it
                    vm.setQuery(it)
                },
                placeholder = {
                    Text(
                        text = "Enter search keyword"
                    )
                })
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Black)
            )
            val poiState by vm.poiFlow.collectAsState(initial = emptyList())
            if (poiState.isEmpty()) {
//                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center){
//                    Text("Entry search keyword", color = Color.Gray)
//                }
            } else {
                poiState.forEach {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            viewLifecycleOwner.lifecycle.coroutineScope.launchWhenStarted {
                                vm.clickPoi(it)
                                activity?.onBackPressed()
                            }
                        }) {
                        Text(text = it.name, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}