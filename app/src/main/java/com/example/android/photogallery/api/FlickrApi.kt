package com.example.android.photogallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface FlickrApi {

    /** аннотация метода HTTP-запроса.
     * Указание типа Call<FlickrResponse> инструктирует Retrofit,
     * чтобы ответ десериализовался в объект FlickrResponse */

    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
                "&api_key=2e5a9fdb9e0131dd1944bbc1c527cb56" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s"
    )
    fun fetchPhotos(): Call<FlickrResponse>

    /** Использование аннотации @GET в сочетании с аннотацией @Url приводит к тому,
     * что Retrofit полностью переопределяет базовый URL.
     * Вместо этого Retrofit будет использовать URL, переданный в функцию fetchUrlBytes() */
    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>
}