package com.example.android.photogallery

import com.google.gson.annotations.SerializedName

/** класс модели элемента галереи */

data class GalleryItem(
    var title: String = "",
    var id: String = "",
    @SerializedName("url_s") var url: String = ""
)
