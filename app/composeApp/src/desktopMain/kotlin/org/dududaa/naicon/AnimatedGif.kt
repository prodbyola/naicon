package org.dududaa.naicon

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedPlayIcon() {
    val lineColor = Color.White
    val innerBoxColor = Color.Red

    val boxHeight = 154.dp
    val boxWidth = 204.dp

    val ibX = 36.dp
    val ibY = 20.dp

    val borderRadius = 24.dp
    val borderWidth = 6.dp

    Box(
        modifier = Modifier
            .height(boxHeight)
            .width(boxWidth)
            .border(
                width = borderWidth,
                color = lineColor,
                shape = RoundedCornerShape(borderRadius)
            )
    ) {
        Spacer(
            Modifier
                .offset(ibX, ibY)
                .background(
                    color = innerBoxColor,
                    shape = RoundedCornerShape(borderRadius)
                )
                .border(
                    width = borderWidth,
                    color = lineColor,
                    shape = RoundedCornerShape(borderRadius)
                )
                .drawWithCache {

                    // play button offset
                    val x = 92F
                    val y = 66F

                    // play button dimensions
                    val width = size.width - (x * 2)
                    val height = size.height - (y * 2)

                    // draw play button
                    val path = Path()
                    path.moveTo(x, y)
                    path.lineTo(x + width, size.height / 2)
                    path.lineTo(x, height + y)
                    path.close()

                    onDrawBehind {
                        drawPath(path, lineColor)
                    }
                }
                .width(boxWidth - (ibX * 2))
                .height(boxHeight - (ibY * 2))
        )
    }
}