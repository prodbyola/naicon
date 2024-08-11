package org.dududaa.naicon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import naicon.composeapp.generated.resources.Res
import naicon.composeapp.generated.resources.bg

fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context).crossfade(true).logger(DebugLogger()).build()

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun App() {
    val bgOverlayAlpha = .8F

    NaiconTheme {
        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }

        val colorScheme = MaterialTheme.colorScheme

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Background Image
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(Res.drawable.bg),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }

            // Background Overlay
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                colorScheme.background.copy(alpha = bgOverlayAlpha),
                                colorScheme.background.copy(alpha = bgOverlayAlpha - .4f)
                            )
                        )
                    )
            )

            // Content
            Box(
                contentAlignment = Alignment.Center,
//                modifier = Modifier.width(400.dp)
            ) {

                AnimatedPlayIcon()
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .offset(0.dp, 130.dp)
                ) {
                    val brush = Brush.linearGradient(
                        listOf(
                            colorScheme.surface,
                            colorScheme.secondary,
                            colorScheme.primary.copy(red = 1f),
                        )
                    )

                    Text(
                        "Native Converter".uppercase(),
                        style = TextStyle(
                            brush = brush
                        ),
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontFamily = poppinsFontFamily
                    )
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = colorScheme.background
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Get Started", fontFamily = poppinsFontFamily)
                    }
                }
            }

        }
    }
}