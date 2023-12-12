package dev.raduhs.newsapp.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import dev.raduhs.newsapp.network.NewsManager
import dev.raduhs.newsapp.ui.screen.Categories
import dev.raduhs.newsapp.ui.screen.DetailScreen
import dev.raduhs.newsapp.ui.screen.Sources
import dev.raduhs.newsapp.ui.screen.TopNews

@Composable
fun NewsApp() {
    val navController = rememberNavController()
    val scrollState = rememberScrollState()

    MainScreen(navController, scrollState)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController, scrollState: ScrollState) {

    Scaffold(bottomBar = { BottomMenu(navController = navController)}) {
        Navigation(navController, scrollState)
    }
}

@Composable
fun Navigation(navController: NavHostController, scrollState: ScrollState, newsManager: NewsManager = NewsManager()) {

    val articles = newsManager.newsResponse.value.articles
    Log.d("news", "$articles")

    NavHost(navController = navController, startDestination = "TopNews") {
        bottomNavigation(navController)
        composable("TopNews") {
            TopNews(navController = navController)
        }

        composable("Detail/{newsId}",
            arguments = listOf(navArgument("newsId"){type= NavType.IntType})
        ) {
            navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt("newsId")
            val newsData = MockData.getNews(id)
            DetailScreen(navController = navController, newsData, scrollState = scrollState)
        }
    }
}

fun NavGraphBuilder.bottomNavigation(navController: NavHostController) {
    composable(BottomMenuScreen.TopNews.route) {
        TopNews(navController = navController)
    }
    composable(BottomMenuScreen.Categories.route) {
        Categories()
    }
    composable(BottomMenuScreen.Sources.route) {
        Sources()
    }
}