package com.example.android.photogallery.api

import com.example.android.photogallery.GalleryItem
import com.google.gson.annotations.SerializedName

/** класс для сопоставления с объектом "photos" JSON-данных */

class PhotoResponse {
    @SerializedName("photo")
    lateinit var galleryItems: List<GalleryItem>
}