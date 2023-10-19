package com.example.android.photogallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrApi {

    /** аннотация метода HTTP-запроса.
     * Указание типа Call<FlickrResponse> инструктирует Retrofit,
     * чтобы ответ десериализовался в объект FlickrResponse */

    @GET("services/rest?method=flickr.interestingness.getList")
    fun fetchPhotos(): Call<FlickrResponse>

    /** Использование аннотации @GET в сочетании с аннотацией @Url приводит к тому,
     * что Retrofit полностью переопределяет базовый URL.
     * Вместо этого Retrofit будет использовать URL, переданный в функцию fetchUrlBytes() */
    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>

    @GET("services/rest?method=flickr.photos.search")
    fun searchPhotos(@Query("text") query: String): Call<FlickrResponse>
}