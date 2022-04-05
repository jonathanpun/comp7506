package cs.hku.comp7506.ui.create

import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cs.hku.comp7506.databinding.FragmentCreateBinding
import cs.hku.comp7506.databinding.FragmentHomeBinding

class CreateFragment:Fragment() {
    private var _binding:FragmentCreateBinding? = null
    private val binding:FragmentCreateBinding
        get() = _binding!!
    private val vm: CreateViewModel by viewModels()

    val content = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        vm.addImage(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBinding.inflate(inflater)
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
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}