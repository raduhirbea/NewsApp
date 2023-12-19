package dev.raduhs.newsapp.network

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import dev.raduhs.newsapp.models.ArticleCategory
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

     suspend fun getArticlesByCategory(category: String) : TopNewsResponse =
         withContext(Dispatchers.IO) {
             service.getArticlesByCategory(category)
         }

    fun getArticlesBySource() {
        val service = Api.retrofitService.getArticlesBySource(sourceName.value)
        service.enqueue(object : Callback<TopNewsResponse> {

            override fun onResponse(
                call: Call<TopNewsResponse>,
                response: Response<TopNewsResponse>
            ) {
                if (response.isSuccessful) {
                    _getArticlesBySource.value= response.body()!!
                    Log.d("source", "${_getArticlesBySource.value}")
                } else {
                    Log.d("error", "${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<TopNewsResponse>, t: Throwable) {
                Log.d("error", "${t.printStackTrace()}")
            }

        })
    }

    fun getSearchedArticles(query:String) {
        val service = Api.retrofitService.getArticles(query)
        service.enqueue(object : Callback<TopNewsResponse> {

            override fun onResponse(
                call: Call<TopNewsResponse>,
                response: Response<TopNewsResponse>
            ) {
                if (response.isSuccessful) {
                    _searchedNewsResponse.value= response.body()!!
                    Log.d("search", "${_searchedNewsResponse.value}")
                } else {
                    Log.d("search", "${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<TopNewsResponse>, t: Throwable) {
                Log.d("search error", "${t.printStackTrace()}")
            }

        })
    }
}