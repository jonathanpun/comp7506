package cs.hku.comp7506.ui.create

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.hku.comp7506.model.Poi

class CreateViewModel: ViewModel() {
    private val _images:MutableLiveData<List<Uri>> = MutableLiveData(emptyList())
    val images:LiveData<List<Uri>> = _images
    private val _content:MutableLiveData<String> = MutableLiveData()
    val content:LiveData<String> = _content
    private val _poi:MutableLiveData<Poi?> = MutableLiveData()
    val poi:LiveData<Poi?> = _poi

    fun addImage(images:List<Uri>){
        _images.value = images.take(3)
    }

    fun removeImage(index:Int){
        _images.value= _images.value?.toMutableList()?.apply {  removeAt(index)}
    }
}