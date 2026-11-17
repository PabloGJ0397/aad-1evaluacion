package edu.pablogj0397.add_1evaluacion.core.cache

import android.content.Context
import edu.pablogj0397.add_1evaluacion.R
import edu.pablogj0397.add_1evaluacion.core.time.AppTime

interface AppCache {
    fun updateTime(key: String)
    fun isExpired(key: String, ttl: Long): Boolean
}

class AppCacheTTL (
    private val context: Context,
    private val appTime: AppTime) : AppCache{

    private val sharedPref = context.getSharedPreferences(context.getString(R.string.local_app_cache),
        Context.MODE_PRIVATE)
    override fun updateTime(key: String) {
        sharedPref.edit().apply {
            putLong(key,appTime.currentTime())
            apply()
        }
    }

    override fun isExpired(key: String, ttl: Long): Boolean {
        val savedTime = sharedPref.getLong(key,-1)
        return appTime.currentTime() - savedTime >= ttl
    }

}