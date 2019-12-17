package ut.ee.cs.rsg

import android.app.Application
import android.util.Log

import java.io.File


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun clearApplicationData() {
        val cache: File = cacheDir
        val appDir = File(cache.getParent())
        if (appDir.exists()) {
            val children: Array<String> = appDir.list()
            for (s in children) {
                if (s != "lib") {
                    deleteDir(File(appDir, s))
                    Log.i("TAG", "File /data/data/APP_PACKAGE/$s DELETED ")
                }
            }
        }
    }

    companion object {
        var instance: MyApplication? = null
            private set

        fun deleteDir(dir: File?): Boolean {
            if (dir != null && dir.isDirectory()) {
                val children: Array<String> = dir.list()
                for (i in children.indices) {
                    val success = deleteDir(File(dir, children[i]))
                    if (!success) {
                        return false
                    }
                }
            }
            return dir!!.delete()
        }
    }
}