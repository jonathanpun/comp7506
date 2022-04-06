package cs.hku.comp7506.ui.create

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import cs.hku.comp7506.databinding.FragmentCreateBinding
import cs.hku.comp7506.util.NavDirection
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect

class CreateFragment:Fragment() {
    private var _binding:FragmentCreateBinding? = null
    private val binding:FragmentCreateBinding
        get() = _binding!!
    private val vm: CreateViewModel by viewModels(factoryProducer = {CreateViewModelFactory(requireActivity().contentResolver)})
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val content = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        vm.addImage(it)
    }
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                getLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                getLocation()
            } else -> {
            // No location access granted.
                context?.let { Toast.makeText(it,"permission required",Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
                }
        }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient= LocationServices.getFusedLocationProviderClient(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBinding.inflate(inflater)
        _binding?.viewmodel = vm
        binding.layoutAddDescription.setOnClickListener {
            content.launch("image/*")
        }
        vm.images.observe(viewLifecycleOwner, Observer {
            val imageViews= listOf(binding.imageviewImage1,binding.imageviewImage2,binding.imageviewImage3)
            val imageViewsCloses = listOf(binding.imageviewClose1,binding.imageviewClose2,binding.imageviewClose3)
            imageViews.forEachIndexed { index, imageView ->
                val uri = it.getOrNull(index)
                if(uri == null){
                    imageView.visibility=View.GONE
                    imageViewsCloses.getOrNull(index)?.visibility=View.GONE
                }else{
                    imageView.visibility=View.VISIBLE
                    imageView.setImageURI(uri)
                    imageViewsCloses.getOrNull(index)?.visibility =View.VISIBLE
                    imageViewsCloses.getOrNull(index)?.setOnClickListener {
                        vm.removeImage(index)
                    }
                }
            }
            binding.layoutAddDescription.visibility= if(it.isEmpty())View.VISIBLE else View.GONE
        })
        vm.poi.observe(viewLifecycleOwner, Observer {
            binding.textviewLocation.text= "${it?.poi?.name}, ${it?.dist}m away"
        })
        vm.submitButton.observe(viewLifecycleOwner){
            binding.buttonCreate.isEnabled=it
        }
        binding.buttonCreate.setOnClickListener {
            vm.createPost()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.navFlow.collect {
                when(it){
                    is NavDirection.Back->{
                        activity?.onBackPressed()
                    }
                }
            }
        }
        requestLocation()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun requestLocation (){
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }
    @SuppressLint("MissingPermission")
    private fun getLocation(){
          fusedLocationClient.getCurrentLocation(PRIORITY_BALANCED_POWER_ACCURACY,CancellationTokenSource().token).addOnSuccessListener {
                vm.setLocation(it.latitude,it.longitude)
            }
    }
}