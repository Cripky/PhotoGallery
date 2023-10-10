package com.example.android.photogallery.api

import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {

    /** аннотация метода HTTP-запроса.
     * Указание типа Call<String> инструктирует Retrofit,
     * чтобы ответ десериализовался в объект String */
    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
                "&api_key=2e5a9fdb9e0131dd1944bbc1c527cb56" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s"
    )
    fun fetchPhotos(): Call<FlickrResponse>

}