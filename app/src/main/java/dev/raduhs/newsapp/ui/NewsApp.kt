package dev.raduhs.newsapp.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
        Navigation(navController, scrollState, paddingValues = it)
    }
}

@Composable
fun Navigation(navController: NavHostController, scrollState: ScrollState, newsManager: NewsManager = NewsManager(), paddingValues: PaddingValues) {

    val articles = newsManager.newsResponse.value.articles
    Log.d("news", "$articles")
    articles?.let {
        NavHost(navController = navController, startDestination = BottomMenuScreen.TopNews.route, modifier = Modifier.padding(paddingValues = paddingValues)) {
            bottomNavigation(navController, articles)

/*            composable("TopNews") {
                TopNews(navController = navController, articles)
            }*/

            composable("Detail/{index}",
                arguments = listOf(navArgument("index"){type= NavType.IntType})
            ) {
                    navBackStackEntry ->
                val index = navBackStackEntry.arguments?.getInt("index")
                index?.let {
                    val article = articles[index]
                    DetailScreen(navController = navController, article, scrollState = scrollState)
                }

            }
        }
    }


}

fun NavGraphBuilder.bottomNavigation(navController: NavHostController, articles: List<TopNewsArticle>) {
    composable(BottomMenuScreen.TopNews.route) {
        TopNews(navController = navController, articles)
    }
    composable(BottomMenuScreen.Categories.route) {
        Categories()
    }
    composable(BottomMenuScreen.Sources.route) {
        Sources()
    }
}