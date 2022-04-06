package cs.hku.comp7506.ui.create

import android.net.Uri
import androidx.lifecycle.*
import cs.hku.comp7506.model.Poi
import cs.hku.comp7506.model.PoiNNResponse
import cs.hku.comp7506.repository.FeedRepository
import cs.hku.comp7506.util.NavDirection
import cs.hku.comp7506.util.combineLatest
import cs.hku.comp7506.util.debug
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateViewModel(val feedRepository: FeedRepository) :ViewModel() {
    private val _images:MutableLiveData<List<Uri>> = MutableLiveData(emptyList())
    val images:LiveData<List<Uri>> = _images
    val content:MutableLiveData<String> = MutableLiveData()
    private val _poi:MutableLiveData<PoiNNResponse?> = MutableLiveData()
    val poi:LiveData<PoiNNResponse?> = _poi
    val submitButton:LiveData<Boolean>
    private val _navFlow: MutableStateFlow<NavDirection?> = MutableStateFlow(null)
    val navFlow:Flow<NavDirection?> = _navFlow
    init {
        submitButton = Transformations.map(combineLatest(content,images,poi)){
            !it.first.isNullOrBlank()&&it.second?.size?:0>0 && it.third!=null
        }
    }

    fun addImage(images:List<Uri>){
        _images.value = images.take(3)
    }

    fun removeImage(index:Int){
        _images.value= _images.value?.toMutableList()?.apply {  removeAt(index)}
    }

    fun setLocation(lat:Double,lon:Double){
         viewModelScope.launch {
            val response= feedRepository.getNearestPoi(lat,lon)
             _poi.value =response
         }
    }
    fun createPost(){
        viewModelScope.launch {
            feedRepository.createPost(images.value!!,content.value!!,poi.value!!)
            _navFlow.emit(NavDirection.Back)
        }
    }
}