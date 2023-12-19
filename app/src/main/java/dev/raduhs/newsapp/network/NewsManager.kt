package dev.raduhs.newsapp.network

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import dev.raduhs.newsapp.models.ArticleCategory
import dev.raduhs.newsapp.models.Source
import dev.raduhs.newsapp.models.TopNewsResponse
import dev.raduhs.newsapp.models.getArticleCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsManager(private val service: NewsService) {

    val sourceName = mutableStateOf("abc-news")

    private val _newsResponse = mutableStateOf(TopNewsResponse())
    val newsResponse: State<TopNewsResponse>
        @Composable get() = remember {
            _newsResponse
        }



    val selectedCategory : MutableState<ArticleCategory?> = mutableStateOf(null)

    private val _getArticlesBySource = mutableStateOf(TopNewsResponse())
    val getArticleBySource: MutableState<TopNewsResponse>
        @Composable get() = remember {
            _getArticlesBySource
        }

    val query = mutableStateOf("")

    private val _searchedNewsResponse = mutableStateOf(TopNewsResponse())
    val searchedNewsResponse: MutableState<TopNewsResponse>
        @Composable get() = remember {
            _searchedNewsResponse
        }

    suspend fun getArticles(country: String): TopNewsResponse =
        withContext(Dispatchers.IO) {
            service.getTopArticles(country)
        }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getArticleCategory(category)
        selectedCategory.value = newCategory
    }

    suspend fun getArticlesByCategory(category: String): TopNewsResponse =
        withContext(Dispatchers.IO) {
            service.getArticlesByCategory(category)
        }

    suspend fun getArticlesBySource(source: String): TopNewsResponse =
        withContext(Dispatchers.IO) {
            service.getArticlesBySource(source)
        }

    suspend fun getSearchedArticles(query:String) : TopNewsResponse =
        withContext(Dispatchers.IO) {
            service.getArticles(query)
        }
}