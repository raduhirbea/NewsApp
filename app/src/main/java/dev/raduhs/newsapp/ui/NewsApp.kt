package dev.raduhs.newsapp.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

    val articles = mutableListOf(TopNewsArticle())
    val topArticles = viewModel.newsResponse.collectAsState().value.articles
    articles.addAll(topArticles?: listOf())

    Log.d("news", "$articles")
    articles?.let {
        NavHost(navController = navController, startDestination = BottomMenuScreen.TopNews.route, modifier = Modifier.padding(paddingValues = paddingValues)) {
            bottomNavigation(navController, articles, newsManager, viewModel)

/*            composable("TopNews") {
                TopNews(navController = navController, articles)
            }*/

            composable("Detail/{index}",
                arguments = listOf(navArgument("index"){type= NavType.IntType})
            ) {
                    navBackStackEntry ->
                val index = navBackStackEntry.arguments?.getInt("index")
                index?.let {
                    if(newsManager.query.value.isNotEmpty()) {
                        articles.clear()
                        articles.addAll(newsManager.searchedNewsResponse.value.articles?: listOf())
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

fun NavGraphBuilder.bottomNavigation(navController: NavHostController, articles: List<TopNewsArticle>, newsManager: NewsManager, viewModel: MainViewModel) {
    composable(BottomMenuScreen.TopNews.route) {
        TopNews(navController = navController, articles, newsManager.query, newsManager)
    }
    composable(BottomMenuScreen.Categories.route) {
        viewModel.getArticlesByCategory("business")
        viewModel.onSelectedCategoryChanged("business")

        Categories(viewModel = viewModel, onFetchCategory = {
            viewModel.onSelectedCategoryChanged(it)
            viewModel.getArticlesByCategory(it)
        })
    }
    composable(BottomMenuScreen.Sources.route) {
        Sources(newsManager = newsManager)
    }
}