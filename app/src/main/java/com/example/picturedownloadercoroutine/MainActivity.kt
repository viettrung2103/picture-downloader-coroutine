package com.example.picturedownloadercoroutine

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Picture
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.TypedArrayUtils.getText
import com.example.picturedownloadercoroutine.ui.theme.PictureDownloaderCoroutineTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val urlText = URL("https://users.metropolia.fi/~jarkkov/folderimage.jpg")
        setContent {
            showImage(urlText)
        }
    }
}
@Composable
fun showImage(urlText: URL) {
    val ctx = LocalContext.current
    var bitmap by remember {mutableStateOf<Bitmap?>(null)}
    var error  by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(urlText) {
        try{
        bitmap = loadBitmapFromUrl(urlText)
        } catch (e: Exception){
            error = e.message
        }
    }
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)) {
        Text(
            text = "Trung Doan showing test file from internet"
        )
        Text(
            text = "$urlText",
            modifier = Modifier.padding(30.dp)
        )
        Box(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxSize(),

        ){
            when {
                bitmap != null -> Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null
                )
                error != null -> Text("error loading page: $error")
                else -> Text("Loading...")

            }
        }

    }
}
// reads a file
private suspend fun loadBitmapFromUrl(url: URL): Bitmap =
    withContext(Dispatchers.IO) {
        url.openStream().use { stream ->
            BitmapFactory.decodeStream(stream)?: error("Fail to decode image")
        }
    }
