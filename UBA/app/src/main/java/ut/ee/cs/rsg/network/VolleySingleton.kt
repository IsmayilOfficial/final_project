package ut.ee.cs.rsg.network

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley

class VolleySingleton private constructor(private val mCtx: Context) {
    private var mRequestQueue: RequestQueue?
    val imageLoader: ImageLoader
    val requestQueue: RequestQueue?
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mCtx.applicationContext)
            }
            return mRequestQueue
        }

    fun <T> addToRequestQueue(req: Request<T>?) {
        requestQueue!!.add(req)
    }

    companion object {
        private var mInstance: VolleySingleton? = null
        @Synchronized
        fun getInstance(context: Context): VolleySingleton? {
            if (mInstance == null) {
                mInstance = VolleySingleton(context)
            }
            return mInstance
        }
    }

    init {
        mRequestQueue = requestQueue
        imageLoader = ImageLoader(mRequestQueue, object : ImageLoader.ImageCache {
            private val cache = LruCache<String, Bitmap>(20)
            override fun getBitmap(url: String): Bitmap {
                return cache[url]
            }

            override fun putBitmap(url: String, bitmap: Bitmap) {
                cache.put(url, bitmap)
            }
        })
    }
}