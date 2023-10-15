package com.example.android.photogallery.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.photogallery.FlickrRepository
import com.example.android.photogallery.GalleryItem

/** получение данных из "репозитория" FlickrRepository */

class PhotoGalleryViewModel : ViewModel() {

    val galleryItemLiveData: LiveData<List<GalleryItem>>

    init {
        galleryItemLiveData = FlickrRepository().fetchPhotos()
    }

}