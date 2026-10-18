package com.example.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

/**
 * Material 3 Banner Ad component integrating Google AdMob Banner Ads.
 * Complies with Google Play policy by clearly labelling sponsored ad space.
 */
@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adUnitId: String = AdManager.TEST_BANNER_AD_UNIT_ID,
    showLabel: Boolean = true
) {
    val isInEditMode = LocalInspectionMode.current
    if (isInEditMode) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Google Ads Banner Preview",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    var isAdLoaded by remember { mutableStateOf(false) }
    var adViewInstance by remember { mutableStateOf<AdView?>(null) }

    DisposableEffect(lifecycleOwner, adViewInstance) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> adViewInstance?.pause()
                Lifecycle.Event.ON_RESUME -> adViewInstance?.resume()
                Lifecycle.Event.ON_DESTROY -> adViewInstance?.destroy()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            adViewInstance?.destroy()
        }
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(Color(0x55FFFFFF), Color(0x1538BDF8))
                ),
                RoundedCornerShape(14.dp)
            )
            .testTag("banner_ad_container"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.65f)
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showLabel) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "QUẢNG CÁO",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Google Ads Partner",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    modifier = Modifier.testTag("google_ad_view"),
                    factory = { context ->
                        AdView(context).apply {
                            try {
                                setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null)
                            } catch (_: Exception) {}
                            setAdSize(AdSize.BANNER)
                            this.adUnitId = adUnitId
                            adListener = object : AdListener() {
                                override fun onAdLoaded() {
                                    isAdLoaded = true
                                }

                                override fun onAdFailedToLoad(error: LoadAdError) {
                                    isAdLoaded = false
                                }
                            }
                            loadAd(AdRequest.Builder().build())
                            adViewInstance = this
                        }
                    }
                )
            }
        }
    }
}
