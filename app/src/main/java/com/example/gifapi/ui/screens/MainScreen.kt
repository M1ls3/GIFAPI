package com.example.gifapi.ui.screens

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build.VERSION.SDK_INT
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.grid.items
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.gifapi.Data.GifData
import com.example.gifapi.ViewModel.MainViewModel
import com.example.gifapi.ViewModel.UiState

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        if (!context.isNetworkAvailable()) {
            Toast.makeText(context, "Виникла помилка", Toast.LENGTH_SHORT).show()
        }
    }

    when (val state = viewModel.uiState.value) {
        is UiState.Loading -> CenterProgress()
        is UiState.Success -> GifGrid(gifs = state.gifs)
        is UiState.Error -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Виникла помилка", Toast.LENGTH_SHORT).show()
            }
            ErrorState()
        }
    }
}

@Composable
fun GifGrid(gifs: List<GifData>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(gifs) { gif ->
            GifItem(gif = gif)
        }
    }
}

@Composable
fun GifItem(gif: GifData) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Card(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .clickable {
                val intent = Intent(context, FullscreenActivity::class.java).apply {
                    putExtra("gif_url", gif.images.fullSize.url)
                }
                context.startActivity(intent)
            }
    ) {
        AsyncImage(
            model = gif.images.preview.url,
            contentDescription = "GIF Preview",
            imageLoader = imageLoader,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CenterProgress() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Помилка завантаження даних")
    }
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetworkInfo?.isConnected == true
}