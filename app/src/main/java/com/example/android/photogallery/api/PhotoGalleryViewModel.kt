package com.example.android.photogallery.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.photogallery.FlickrFetch
import com.example.android.photogallery.GalleryItem

/** получение данных из "репозитория" FlickrFetch */

class PhotoGalleryViewModel : ViewModel() {

    val galleryItemLiveData: LiveData<List<GalleryItem>>

    init {
        galleryItemLiveData = FlickrFetch().fetchPhotos()
    }

}