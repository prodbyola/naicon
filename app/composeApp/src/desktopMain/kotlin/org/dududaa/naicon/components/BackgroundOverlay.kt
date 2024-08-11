package org.dududaa.naicon.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

@Composable
fun BackgroundOverlay(){
    val colorScheme = MaterialTheme.colorScheme
    val bgOverlayAlpha = .8f

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
}