package cs.hku.comp7506.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cs.hku.comp7506.R
import cs.hku.comp7506.adapter.FeedAdapter
import cs.hku.comp7506.adapter.convertToFeedDisplayModel
import cs.hku.comp7506.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val adapter = FeedAdapter()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this,HomeViewModelFactory()).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.apply {
            recyclerview.adapter = adapter
            recyclerview.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }

        homeViewModel.feed.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.convertToFeedDisplayModel())
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}