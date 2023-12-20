package dev.raduhs.newsapp.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.raduhs.newsapp.BottomMenuScreen
import dev.raduhs.newsapp.MockData
import dev.raduhs.newsapp.components.BottomMenu
import dev.raduhs.newsapp.models.TopNewsArticle
import dev.raduhs.newsapp.network.Api
import dev.raduhs.newsapp.network.NewsManager
import dev.raduhs.newsapp.ui.screen.Categories
import dev.raduhs.newsapp.ui.screen.DetailScreen
import dev.raduhs.newsapp.ui.screen.Sources
import dev.raduhs.newsapp.ui.screen.TopNews

@Composable
fun NewsApp(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val scrollState = rememberScrollState()

    MainScreen(navController, scrollState, mainViewModel)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController, scrollState: ScrollState, mainViewModel: MainViewModel) {

    Scaffold(bottomBar = { BottomMenu(navController = navController)}) {
        Navigation(navController, scrollState, paddingValues = it, viewModel = mainViewModel)
    }
}

@Composable
fun Navigation(navController: NavHostController, scrollState: ScrollState, newsManager: NewsManager = NewsManager(
    Api.retrofitService), paddingValues: PaddingValues, viewModel: MainViewModel) {

    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.isError.collectAsState()

    val articles = mutableListOf(TopNewsArticle())
    val topArticles = viewModel.newsResponse.collectAsState().value.articles
    articles.addAll(topArticles?: listOf())

    Log.d("news", "$articles")
    articles?.let {
        NavHost(navController = navController, startDestination = BottomMenuScreen.TopNews.route, modifier = Modifier.padding(paddingValues = paddingValues)) {

            val queryState = mutableStateOf(viewModel.query.value)
            val isLoading = mutableStateOf(loading)
            val isError = mutableStateOf(error)

            bottomNavigation(navController, articles, query = queryState, viewModel, isLoading, isError)

/*            composable("TopNews") {
                TopNews(navController = navController, articles)
            }*/

            composable("Detail/{index}",
                arguments = listOf(navArgument("index"){type= NavType.IntType})
            ) {
                    navBackStackEntry ->
                val index = navBackStackEntry.arguments?.getInt("index")
                index?.let {
                    if(queryState.value != "") {
                        articles.clear()
                        articles.addAll(viewModel.searchedNewsResponse.value.articles?: listOf())
                    } else {
                        articles.clear()
                        articles.addAll(viewModel.newsResponse.value.articles?: listOf())
                    }
                    val article = articles[index]
                    DetailScreen(navController = navController, article, scrollState = scrollState)
                }

            }
        }
    }


}

fun NavGraphBuilder.bottomNavigation(
    navController: NavHostController,
    articles: List<TopNewsArticle>,
    query: MutableState<String>,
    viewModel: MainViewModel,
    isLoading:MutableState<Boolean>,
    isError:MutableState<Boolean>,
) {
    composable(BottomMenuScreen.TopNews.route) {
        TopNews(navController = navController, articles, query, viewModel = viewModel, isLoading, isError)
    }
    composable(BottomMenuScreen.Categories.route) {
        viewModel.getArticlesByCategory("business")
        viewModel.onSelectedCategoryChanged("business")

        Categories(viewModel = viewModel, onFetchCategory = {
            viewModel.onSelectedCategoryChanged(it)
            viewModel.getArticlesByCategory(it)
        }, isLoading = isLoading, isError = isError)
    }
    composable(BottomMenuScreen.Sources.route) {
        Sources(viewModel, isLoading, isError)
    }
}