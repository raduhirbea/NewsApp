package dev.raduhs.newsapp.data

import dev.raduhs.newsapp.network.NewsManager

class Repository(val manager: NewsManager) {

    suspend fun getArticles() = manager.getArticles("us")
    suspend fun getArticlesByCategory(category:String) = manager.getArticlesByCategory(category)
}