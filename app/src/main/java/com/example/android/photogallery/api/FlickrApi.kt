package com.example.android.photogallery.api

import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {

    /** аннотация метода HTTP-запроса.
    * Указание типа Call<String> инструктирует Retrofit,
    * чтобы ответ десериализовался в объект String */
    @GET("/")
    fun fetchContents(): Call<String>

}