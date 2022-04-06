package cs.hku.comp7506.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cs.hku.comp7506.R
import cs.hku.comp7506.adapter.FeedAdapter
import cs.hku.comp7506.adapter.convertToFeedDisplayModel
import cs.hku.comp7506.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val adapter = FeedAdapter().apply {
        onPlaceClicked = fun(displayModel){
            if ( displayModel.feed.geoPoint==null)
                return
            // Create a Uri from an intent string. Use the result to create an Intent.
            val gmmIntentUri = Uri.parse("geo:${displayModel.feed.geoPoint.latitude},${displayModel.feed.geoPoint.longitude}?q=${displayModel.feed.geoPoint.latitude},${displayModel.feed.geoPoint.longitude}")
            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps")
            // Attempt to start an activity that can handle the Intent
            startActivity(mapIntent)
        }
        onPoiClicked = fun(displayModel){
            if ( displayModel.feed.poi==null)
                return
            // Create a Uri from an intent string. Use the result to create an Intent.
            val gmmIntentUri = Uri.parse("geo:0,0?q=${displayModel.feed.poi.geoPoint.latitude},${displayModel.feed.poi.geoPoint.longitude}(${displayModel.feed.poi.name})")
            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps")
            // Attempt to start an activity that can handle the Intent
            startActivity(mapIntent)
        }
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this, HomeViewModelFactory(contentResolver = requireActivity().contentResolver)).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.apply {
            recyclerview.adapter = adapter
            recyclerview.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        homeViewModel.feed.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.convertToFeedDisplayModel())
        })
        homeViewModel.title.observe(viewLifecycleOwner){
            binding.collapsingToolbarLayout.title = it
            binding.toolbar.title = it
        }
        binding.fab.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToCreateFragment())
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}