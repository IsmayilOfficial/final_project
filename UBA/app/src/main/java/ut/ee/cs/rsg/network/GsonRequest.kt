package ut.ee.cs.rsg.network

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.UnsupportedEncodingException

class GsonRequest<T> : Request<T> {
    private var mGson = Gson()
    private var clazz: Class<T>
    private var headers: Map<String, String>? = null
    private var params: Map<String, String>? = null
    private var listener: Response.Listener<T>

    /**
     * Make a GET request and return a parsed object from JSON.
     */
    constructor(method: Int, url: String?, clazz: Class<T>, listener: Response.Listener<T>, errorListener: Response.ErrorListener?) : super(method, url, errorListener) {
        this.clazz = clazz
        this.listener = listener
        mGson = Gson()
    }

    /**
     * Make a POST request and return a parsed object from JSON.
     */
    constructor(method: Int, url: String?, clazz: Class<T>, params: Map<String, String>?, listener: Response.Listener<T>, errorListener: Response.ErrorListener?) : super(method, url, errorListener) {
        this.clazz = clazz
        this.params = params
        this.listener = listener
        headers = null
        mGson = Gson()
    }

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        return if (headers != null) headers!! else super.getHeaders()
    }

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String> {
        return params!!
    }

    override fun deliverResponse(response: T) {
        listener.onResponse(response)
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<T> {
        return try {
            val json = HttpHeaderParser.parseCharset(response.headers)
            Response.success(mGson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        }
    }
}