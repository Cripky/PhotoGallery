package com.example.android.photogallery.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.photogallery.FlickrRepository
import com.example.android.photogallery.GalleryItem
import retrofit2.http.Query

/** получение данных из "репозитория" FlickrRepository */

class PhotoGalleryViewModel : ViewModel() {

    val galleryItemLiveData: LiveData<List<GalleryItem>>

    private val flickrRepository = FlickrRepository()
    private val mutableSearchTerm = MutableLiveData<String>()

    init {
        mutableSearchTerm.value = "planets"

        galleryItemLiveData = Transformations.switchMap(mutableSearchTerm) { searchTerm ->
            flickrRepository.searchPhotos(searchTerm)
        }
    }

    fun fetchPhotos(query: String = "") {
        mutableSearchTerm.value = query
    }

}