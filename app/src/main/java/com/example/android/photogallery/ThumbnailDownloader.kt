package com.example.android.photogallery

import android.annotation.SuppressLint
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.util.concurrent.ConcurrentHashMap

private const val TAG = "ThumbnailDownloader"

/** Значение MESSAGE_DOWNLOAD будет использоваться для идентификации
 * сообщений как запросов на загрузку */

private const val MESSAGE_DOWNLOAD = 0

class ThumbnailDownloader<in T> : HandlerThread(TAG), LifecycleObserver {

    private var hasQuit = false

    /** В переменной requestHandler будет храниться ссылка на объект Handler,
     * отвечающий за постановку в очередь запросов на загрузку в фоновом потоке ThumbnailDownloader.
     * Этот объект также будет отвечать за обработку сообщений запросов на загрузку
     * при извлечении их из очереди */
    private lateinit var requestHandler: Handler

    /** Использование объекта-идентификатора типа T запроса на загрузку в качестве ключа
     * позволяет хранить и загружать URL-адрес, связанный с конкретным запросом */
    private val requestMap = ConcurrentHashMap<T, String>()


    private val flickrRepository = FlickrRepository()

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("HandlerLeak")
    // Функция onLooperPrepared() вызывается до того, как Looper впервые проверит очередь
    override fun onLooperPrepared() {
        requestHandler = object : Handler() {
            // В handleMessage(...) мы проверяем тип сообщения,
            // читаем значение obj (которое имеет тип T и служит идентификатором для запроса)
            // и передаем его функции handleRequest()
            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    val target = msg.obj as T
                    Log.i(TAG, "Got a request for URL: ${requestMap[target]}")
                    handleRequest(target)
                }
            }
        }
    }

    // Функция quit() завершает поток
    override fun quit(): Boolean {
        hasQuit = true
        return super.quit()
    }

    // аннотация @OnLifecycleEvent(Lifecycle.Event) позволяет ассоциировать функцию в классе
    // с обратным вызовом жизненного цикла. При вызове функции onCreate() вызывается функция setup().
    // функция start() запускает поток
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun setup() {
        Log.i(TAG, "Starting background thread")
        start()
        looper
    }

    // При вызове функции onDestroy() вызывается функция tearDown()
    // Функция quit() завершает поток
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun tearDown() {
        Log.i(TAG, "Destroying background thread")
        quit()
    }

    fun queueThumbnail(target: T, url: String) {
        Log.i(TAG, "Got a URL: $url")
        requestMap[target] = url

        // постановка нового сообщения в очередь сообщений фонового потока
        // obtain - получить
        requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
            .sendToTarget()
    }

    // функция загрузки картинки по url
    private fun handleRequest(target: T) {
        val url = requestMap[target] ?: return
        val bitmap = flickrRepository.fetchPhoto(url) ?: return
    }
}