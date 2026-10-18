package com.example.ui.components.glass

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Liquid Glass Specular Edge Gradients for frosted glassmorphic highlights.
 */
val LiquidGlassBorderGradient = Brush.linearGradient(
    colors = listOf(
        Color(0x99FFFFFF), // Top-left specular highlight (60% white)
        Color(0x3338BDF8), // Cyan shimmer
        Color(0x10FFFFFF), // Translucent edge
        Color(0x50A78BFA)  // Violet refraction rim
    )
)

val LiquidGlassInnerGlowGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0x55FFFFFF), // Glossy top refraction
        Color(0x00FFFFFF)
    )
)

val LiquidGlassFillGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0x38FFFFFF), // 22% white highlight at top
        Color(0x181E293B), // Translucent dark-slate body
        Color(0x280F172A)  // Deep glassy bottom
    )
)

val LiquidGlassSubtleFill = Brush.verticalGradient(
    colors = listOf(
        Color(0x2538BDF8), // Subtle cyan tint
        Color(0x151E293B)
    )
)

/**
 * Liquid Glass Background with living, organic liquid orbs that animate subtly.
 * Glass cards on top naturally refract and reveal these glowing colors.
 */
@Composable
fun LiquidGlassAtmosphere(
    modifier: Modifier = Modifier,
    isLiquidGlassActive: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    if (!isLiquidGlassActive) {
        Box(modifier = modifier) {
            content()
        }
        return
    }

    val infiniteTransition = rememberInfiniteTransition(label = "LiquidGlassTransition")
    
    val orbAnimX by infiniteTransition.animateFloat(
        initialValue = -50f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OrbAnimX"
    )

    val orbAnimY by infiniteTransition.animateFloat(
        initialValue = -40f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 11000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OrbAnimY"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090D1A))
            .drawBehind {
                val canvasW = size.width
                val canvasH = size.height

                // Liquid Orb 1: Luminous Electric Cyan (Top-Left)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF00E5FF).copy(alpha = 0.38f),
                            Color(0xFF00E5FF).copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        center = Offset(
                            x = canvasW * 0.2f + orbAnimX,
                            y = canvasH * 0.18f + orbAnimY
                        ),
                        radius = canvasW * 0.65f
                    )
                )

                // Liquid Orb 2: Ethereal Neon Violet (Top-Right / Center)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF8A2BE2).copy(alpha = 0.35f),
                            Color(0xFF8A2BE2).copy(alpha = 0.10f),
                            Color.Transparent
                        ),
                        center = Offset(
                            x = canvasW * 0.82f - orbAnimX,
                            y = canvasH * 0.35f - orbAnimY
                        ),
                        radius = canvasW * 0.70f
                    )
                )

                // Liquid Orb 3: Radiant Sunset Coral / Magenta (Mid-Bottom)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF2A85).copy(alpha = 0.28f),
                            Color(0xFFFF2A85).copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        center = Offset(
                            x = canvasW * 0.3f - orbAnimY,
                            y = canvasH * 0.72f + orbAnimX
                        ),
                        radius = canvasW * 0.65f
                    )
                )

                // Liquid Orb 4: Oceanic Deep Teal (Bottom-Right)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF00B4D8).copy(alpha = 0.30f),
                            Color.Transparent
                        ),
                        center = Offset(
                            x = canvasW * 0.85f + orbAnimX * 0.5f,
                            y = canvasH * 0.88f
                        ),
                        radius = canvasW * 0.60f
                    )
                )
            }
    ) {
        content()
    }
}

/**
 * Liquid Glass Card: Premium frosted glass surface with specular highlight borders
 * and subtle light reflection for depth and tactile beauty.
 */
@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    borderGradient: Brush = LiquidGlassBorderGradient,
    borderWidth: Dp = 1.2.dp,
    elevation: Dp = 8.dp,
    testTag: String = "liquid_glass_card",
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .testTag(testTag)
            .shadow(
                elevation = elevation,
                shape = shape,
                spotColor = Color(0x6000E5FF),
                ambientColor = Color(0x35000000)
            )
            .clip(shape)
            .background(LiquidGlassFillGradient)
            .border(
                BorderStroke(borderWidth, borderGradient),
                shape = shape
            )
            .then(clickableModifier)
    ) {
        // Specular top light reflection sheen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.5.dp)
                .background(LiquidGlassInnerGlowGradient)
                .align(Alignment.TopCenter)
        )

        content()
    }
}

/**
 * Liquid Glass Button: Tactile, translucent glass button with specular edge.
 */
@Composable
fun LiquidGlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    borderWidth: Dp = 1.dp,
    testTag: String = "liquid_glass_btn",
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .testTag(testTag)
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0x5538BDF8),
                        Color(0x250284C7)
                    )
                )
            )
            .border(
                BorderStroke(
                    borderWidth,
                    Brush.linearGradient(
                        listOf(
                            Color(0xCCFFFFFF),
                            Color(0x4038BDF8)
                        )
                    )
                ),
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * Liquid Glass Floating Badge / Pill.
 */
@Composable
fun LiquidGlassPill(
    modifier: Modifier = Modifier,
    testTag: String = "liquid_glass_pill",
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .testTag(testTag)
            .clip(CircleShape)
            .background(Color(0x331E293B))
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(
                        listOf(
                            Color(0x80FFFFFF),
                            Color(0x2038BDF8)
                        )
                    )
                ),
                shape = CircleShape
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * Universal Glass Card that automatically morphs between Liquid Glass and standard M3 theme.
 */
@Composable
fun AppGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    borderGradient: Brush = LiquidGlassBorderGradient,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 6.dp,
    fillBrush: Brush = LiquidGlassFillGradient,
    testTag: String? = null,
    onClick: (() -> Unit)? = null,
    contentPadding: androidx.compose.foundation.layout.PaddingValues = androidx.compose.foundation.layout.PaddingValues(16.dp),
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    val isGlass = com.example.ui.theme.LocalIsLiquidGlass.current
    if (isGlass) {
        val tagModifier = if (testTag != null) Modifier.testTag(testTag) else Modifier
        val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        Box(
            modifier = modifier
                .then(tagModifier)
                .shadow(
                    elevation = elevation,
                    shape = shape,
                    spotColor = Color(0x6000E5FF),
                    ambientColor = Color(0x35000000)
                )
                .clip(shape)
                .background(fillBrush)
                .border(
                    BorderStroke(borderWidth, borderGradient),
                    shape = shape
                )
                .then(clickModifier)
        ) {
            // Specular top light reflection sheen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.5.dp)
                    .background(LiquidGlassInnerGlowGradient)
                    .align(Alignment.TopCenter)
            )

            androidx.compose.foundation.layout.Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding)
            ) {
                content()
            }
        }
    } else {
        val tagModifier = if (testTag != null) Modifier.testTag(testTag) else Modifier
        val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        androidx.compose.material3.ElevatedCard(
            modifier = modifier
                .then(tagModifier)
                .then(clickModifier),
            shape = shape,
            elevation = androidx.compose.material3.CardDefaults.elevatedCardElevation(defaultElevation = elevation),
            colors = androidx.compose.material3.CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding)
            ) {
                content()
            }
        }
    }
}

/**
 * Luminous Liquid Glass Floating Action Button (FAB).
 */
@Composable
fun LiquidGlassFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "liquid_glass_fab",
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .testTag(testTag)
            .size(58.dp)
            .shadow(
                elevation = 10.dp,
                shape = CircleShape,
                spotColor = Color(0x9900E5FF),
                ambientColor = Color(0x55000000)
            )
            .clip(CircleShape)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xCC0284C7),
                        Color(0xCC0369A1)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.5.dp,
                    Brush.linearGradient(
                        listOf(
                            Color(0xFFFFFFFF),
                            Color(0x8038BDF8),
                            Color(0x40A78BFA)
                        )
                    )
                ),
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * Translucent frosted glass chip/surface with subtle iridescent outline.
 */
@Composable
fun LiquidGlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    borderGradient: Brush = Brush.linearGradient(listOf(Color(0x80FFFFFF), Color(0x2038BDF8))),
    borderWidth: Dp = 1.dp,
    backgroundColor: Color = Color(0x351E293B),
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(BorderStroke(borderWidth, borderGradient), shape = shape)
            .then(clickModifier)
    ) {
        content()
    }
}
