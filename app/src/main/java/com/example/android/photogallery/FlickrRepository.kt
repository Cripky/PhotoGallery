package com.example.android.photogallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.photogallery.api.FlickrApi
import com.example.android.photogallery.api.FlickrResponse
import com.example.android.photogallery.api.PhotoInterceptor
import com.example.android.photogallery.api.PhotoResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "FlickrRepository"

/** класс репозитория */

class FlickrRepository {

    private val flickrApi: FlickrApi

    /** при создании FlickrRepository создается экземпляр Retrofit */

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(PhotoInterceptor())
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    fun fetchPhotosRequest(): Call<FlickrResponse> {
        return flickrApi.fetchPhotos()
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        return fetchPhotoMetadata(fetchPhotosRequest())
    }

    fun searchPhotosRequest(query: String): Call<FlickrResponse> {
        return flickrApi.searchPhotos(query)
    }

    fun searchPhotos(query: String): LiveData<List<GalleryItem>> {
        return fetchPhotoMetadata(searchPhotosRequest(query))
    }

    /** функция ставит в очередь сетевой запрос и обертывает результат в LiveData */

    private fun fetchPhotoMetadata(flickrRequest: Call<FlickrResponse>): LiveData<List<GalleryItem>> {

        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()

        // установка в очередь
        flickrRequest.enqueue(object : Callback<FlickrResponse> {

            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }

            override fun onResponse(
                call: Call<FlickrResponse>,
                response: Response<FlickrResponse>
            ) {
                Log.d(TAG, "Response received")
                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems
                    ?: mutableListOf()
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value = galleryItems
            }
        })
        return responseLiveData
    }

    /** функция загружает данные по заданному URL-адресу и декодирует их в изображение Bitmap.
     * Аннотация @WorkerThread указывает, что функция должна вызываться только в фоновом потоке */

    @WorkerThread
    fun fetchPhoto(url: String): Bitmap? {
        val response: Response<ResponseBody> = flickrApi.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap=$bitmap from Response=$response")
        return bitmap
    }

}