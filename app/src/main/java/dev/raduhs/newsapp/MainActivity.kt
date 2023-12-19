package dev.raduhs.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import dev.raduhs.newsapp.ui.MainViewModel
import dev.raduhs.newsapp.ui.NewsApp
import dev.raduhs.newsapp.ui.screen.TopNews
import dev.raduhs.newsapp.ui.theme.NewsAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getTopArticles()

        setContent {
            NewsAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NewsApp(viewModel)
                }

            }
        }
    }
}

