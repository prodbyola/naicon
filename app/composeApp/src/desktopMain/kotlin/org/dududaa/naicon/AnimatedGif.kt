package org.dududaa.naicon

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val LINE_COLOR = Color.White
private const val BOX_HEIGHT = 154
private const val BOX_WIDTH = 216

private const val UNDERLAY_Y = 300f

private val UNDERLAY_COLOR = Color(0xFFFC9E4F)
private val INNER_BOX_COLOR = Color(0xFFF4442E)
private val BACKGROUND_COLOR = Color(0xFFF2F3AE)

@Composable
fun AnimatedPlayIcon() {
    Column(verticalArrangement = Arrangement.Center) {
        PlayerBox()
        PlayerArc()
    }
}

@Composable
private fun PlayerBox() {
    // inner box values
    val ibX = 40f
    val ibY = remember { Animatable(20f) }
    val ibHeight = remember { Animatable(BOX_HEIGHT - (ibY.value * 2)) }
    val ibScale = remember { Animatable(1f) }

    // inner box underlay values
    val ibuY = remember { Animatable(UNDERLAY_Y) }
    val ibuPadding = 48

    val hsPadding = 44f
    val hsY = 230f // horizontal stroke y-offset
    val hsXInit = ibuPadding + hsPadding
    val hsX = remember { Animatable(hsXInit) }

    val vsXInit = ibuPadding.toFloat() + 60
    val vsX = remember { Animatable(vsXInit) } // vertical stroke x-offset

    val borderRadius = 24.dp
    val borderWidth = 6.dp

    val ibMove = 40f
    val initialAnimationDuration = 1000
    val ibScaleDuration = 250

    LaunchedEffect("animationKey") {
        launch {
            while (true) {
                // animate parameters
                val animation1 = async {
                    // move inner box up
                    ibY.animateTo(-ibMove, animationSpec = tween(initialAnimationDuration))

                    // scale inner box out
                    ibScale.animateTo(.8f, animationSpec = tween(ibScaleDuration))
                    ibScale.animateTo(1f, animationSpec = tween(ibScaleDuration))

                    delay(500)

                    // restore inner box
                    ibY.animateTo(20f, animationSpec = tween(initialAnimationDuration))

                }

                val animation2 = async {
                    // drag underlay down
                    ibuY.animateTo(
                        UNDERLAY_Y - 180,
                        animationSpec = tween(initialAnimationDuration)
                    )

                    delay(1000)

                    // drag underlay up
                    ibuY.animateTo(
                        UNDERLAY_Y,
                        animationSpec = tween(initialAnimationDuration)
                    )
                }

                val animation3 = async {
                    // expand horizontal stroke
                    hsX.animateTo(
                        hsX.value - (hsPadding - 22),
                        animationSpec = tween(initialAnimationDuration)
                    )

                    // move vertical stroke
                    vsX.animateTo(
                        hsX.value + 248,
                        animationSpec = tween(initialAnimationDuration)
                    )

                    // collapse horizontal stroke
                    hsX.animateTo(
                        hsXInit,
                        animationSpec = tween(initialAnimationDuration)
                    )
                }

                // run all animations asynchronously and wait for
                // them to finish
                awaitAll(animation1, animation2, animation3)

                // set all parameters to their default values
                ibY.snapTo(20f)
                ibScale.snapTo(1f)
                ibuY.snapTo(UNDERLAY_Y)
                hsX.snapTo(hsXInit)
                vsX.snapTo(vsXInit)

                delay(500)
            }
        }
    }

    Box(
        modifier = Modifier
            .height(BOX_HEIGHT.dp)
            .width(BOX_WIDTH.dp)
    ) {
        // Outer Box
        Spacer(
            Modifier
                .border(
                    width = borderWidth,
                    color = LINE_COLOR,
                    shape = RoundedCornerShape(borderRadius)
                )
                .clip(RoundedCornerShape(12.dp))
                .drawBehind {
                    val width = size.width - (ibuPadding * 2)
                    val hsWidth = size.width - hsX.value

                    val vsH = 60f // vertical stroke height

                    // draw box underlay
                    drawRoundRect(
                        color = UNDERLAY_COLOR.copy(alpha = .4f),
                        topLeft = Offset(ibuPadding.toFloat(), -ibuY.value),
                        size = Size(width = width, height = size.height),
                        cornerRadius = CornerRadius(64f)
                    )

                    // draw horizontal stroke
                    drawLine(
                        color = LINE_COLOR,
                        start = Offset(hsX.value, hsY),
                        end = Offset(hsWidth, hsY),
                        strokeWidth = 18f,
                        cap = StrokeCap.Round
                    )

                    // draw vertical line
                    drawLine(
                        color = LINE_COLOR,
                        start = Offset(vsX.value, hsY - (vsH / 2)),
                        end = Offset(vsX.value, hsY + (vsH / 2)),
                        strokeWidth = 18f,
                        cap = StrokeCap.Round
                    )
                }
                .fillMaxSize()
                .background(
                    color = BACKGROUND_COLOR.copy(alpha = .2f),
                    shape = RoundedCornerShape(borderRadius)
                )
        )

        // Inner Box
        Canvas(
            Modifier
                .absoluteOffset(ibX.dp, ibY.value.dp)
                .width(BOX_WIDTH.dp - (ibX.dp * 2))
                .height(ibHeight.value.dp)
                .scale(ibScale.value)
        ) {
            scale(ibScale.value) {
                // box background
                drawRoundRect(
                    color = INNER_BOX_COLOR,
                    cornerRadius = CornerRadius(borderRadius.value + 12)
                )

                // box border
                drawRoundRect(
                    color = LINE_COLOR,
                    style = Stroke(borderWidth.value * 2),
                    cornerRadius = CornerRadius(borderRadius.value)
                )

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

                drawPath(path, Color.White)


            }
        }
    }
}

@Composable
private fun PlayerArc() {
    Spacer(
        modifier = Modifier
            .drawWithContent {
                drawArc(
                    color = LINE_COLOR,
                    startAngle = 210f,
                    sweepAngle = 120f,
                    useCenter = false,
                    style = Stroke(18f),
                    topLeft = Offset(0f, -4f)
                )
            }
            .width(BOX_WIDTH.dp)
            .height(BOX_HEIGHT.dp)
    )
}