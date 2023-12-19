package dev.raduhs.newsapp

import android.app.Application
import dev.raduhs.newsapp.data.Repository
import dev.raduhs.newsapp.network.Api
import dev.raduhs.newsapp.network.NewsManager

class MainApp:Application() {
    private val manager by lazy {
        NewsManager(Api.retrofitService)
    }

    val repository by lazy {
        Repository(manager)
    }
}