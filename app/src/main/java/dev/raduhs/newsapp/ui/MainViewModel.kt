package dev.raduhs.newsapp.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.raduhs.newsapp.MainApp
import dev.raduhs.newsapp.models.ArticleCategory
import dev.raduhs.newsapp.models.TopNewsResponse
import dev.raduhs.newsapp.models.getArticleCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = getApplication<MainApp>().repository

    private val _newsResponse = MutableStateFlow(TopNewsResponse())
    val newsResponse : StateFlow<TopNewsResponse>
        get() = _newsResponse

    private val _isLoading = MutableStateFlow(true)
    val isLoading : StateFlow<Boolean> = _isLoading

    fun getTopArticles() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _newsResponse.value = repository.getArticles()
            _isLoading.value = false
        }
    }

    private val _getArticleByCategory = MutableStateFlow(TopNewsResponse())
    val getArticleByCategoryResponse: StateFlow<TopNewsResponse>
        get() = _getArticleByCategory

    private val _selectedCategory : MutableStateFlow<ArticleCategory?> = MutableStateFlow(null)
    val selectedCategory: StateFlow<ArticleCategory?>
        get() = _selectedCategory

    fun getArticlesByCategory(category:String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _getArticleByCategory.value = repository.getArticlesByCategory(category)
            _isLoading.value = false
        }
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getArticleCategory(category)
        _selectedCategory.value = newCategory
    }

    val sourceName = MutableStateFlow("engadget")
    private val _getArticlesBySource = MutableStateFlow(TopNewsResponse())
    val getArticlesBySource: StateFlow<TopNewsResponse>
        get() = _getArticlesBySource

    val query = MutableStateFlow("")
    private val _searchedNewsResponse = MutableStateFlow(TopNewsResponse())
    val searchedNewsResponse: StateFlow<TopNewsResponse>
        get() = _searchedNewsResponse

    fun getArticlesBySource() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _getArticlesBySource.value = repository.getArticlesBySource(sourceName.value)
            _isLoading.value = false
        }
    }

    fun getSearchedArticles(query:String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _searchedNewsResponse.value = repository.getSearchedArticles(query)
            _isLoading.value = false
        }
    }
}