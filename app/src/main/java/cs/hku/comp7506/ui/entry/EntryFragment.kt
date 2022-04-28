package cs.hku.comp7506.ui.entry

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import cs.hku.comp7506.databinding.FragmentEntryBinding
import cs.hku.comp7506.util.NavDirection
import kotlinx.coroutines.flow.collect

class EntryFragment:Fragment() {
    val vm :EntryViewModel by viewModels(factoryProducer = {EntryViewModelFactory()})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.init()
        vm.animatorEnded()
        lifecycleScope.launchWhenCreated {
            vm.nav.collect {
                when (it) {
                    is NavDirection.Direction -> {
                        findNavController().navigate(it.id)
                    }
                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEntryBinding.inflate(inflater)
        binding.lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                //vm.animatorEnded()
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationRepeat(p0: Animator?) {
            }
        })

        return binding.root
    }
}