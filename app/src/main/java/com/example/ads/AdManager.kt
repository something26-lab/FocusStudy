package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

/**
 * AdManager handles initialization, loading, and presenting Google Mobile Ads (AdMob).
 * Uses official Google sample test ad unit IDs.
 */
object AdManager {
    private const val TAG = "AdManager"

    // Google AdMob official sample test unit IDs
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
    const val TEST_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    private var isInitialized = false
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null

    var isInterstitialLoading = false
        private set
    var isRewardedLoading = false
        private set

    fun initialize(context: Context, onComplete: () -> Unit = {}) {
        if (isInitialized) {
            onComplete()
            return
        }
        try {
            val requestConfig = RequestConfiguration.Builder()
                .setTestDeviceIds(listOf(AdRequest.DEVICE_ID_EMULATOR))
                .build()
            MobileAds.setRequestConfiguration(requestConfig)

            MobileAds.initialize(context) { initializationStatus ->
                isInitialized = true
                Log.d(TAG, "AdMob MobileAds initialized successfully: $initializationStatus")
                loadInterstitial(context)
                loadRewarded(context)
                onComplete()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error initializing MobileAds: ${e.message}")
            onComplete()
        }
    }

    fun loadInterstitial(context: Context) {
        if (interstitialAd != null || isInterstitialLoading) return
        isInterstitialLoading = true

        try {
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(
                context,
                TEST_INTERSTITIAL_AD_UNIT_ID,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        interstitialAd = ad
                        isInterstitialLoading = false
                        Log.d(TAG, "Interstitial Ad loaded")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        interstitialAd = null
                        isInterstitialLoading = false
                        Log.w(TAG, "Interstitial Ad failed to load: ${loadAdError.message}")
                    }
                }
            )
        } catch (e: Exception) {
            isInterstitialLoading = false
            Log.w(TAG, "Error loading Interstitial Ad: ${e.message}")
        }
    }

    fun showInterstitial(
        activity: Activity,
        onAdDismissedOrSkipped: () -> Unit
    ) {
        val ad = interstitialAd
        if (ad != null) {
            try {
                ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Interstitial Ad dismissed")
                        interstitialAd = null
                        loadInterstitial(activity)
                        onAdDismissedOrSkipped()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.w(TAG, "Interstitial Ad failed to show: ${adError.message}")
                        interstitialAd = null
                        loadInterstitial(activity)
                        onAdDismissedOrSkipped()
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "Interstitial Ad showed")
                    }
                }
                ad.show(activity)
            } catch (e: Exception) {
                Log.w(TAG, "Exception showing Interstitial: ${e.message}")
                interstitialAd = null
                onAdDismissedOrSkipped()
            }
        } else {
            // Not ready yet, skip gracefully
            loadInterstitial(activity)
            onAdDismissedOrSkipped()
        }
    }

    fun loadRewarded(context: Context) {
        if (rewardedAd != null || isRewardedLoading) return
        isRewardedLoading = true

        try {
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(
                context,
                TEST_REWARDED_AD_UNIT_ID,
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        rewardedAd = ad
                        isRewardedLoading = false
                        Log.d(TAG, "Rewarded Ad loaded")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        rewardedAd = null
                        isRewardedLoading = false
                        Log.w(TAG, "Rewarded Ad failed to load: ${loadAdError.message}")
                    }
                }
            )
        } catch (e: Exception) {
            isRewardedLoading = false
            Log.w(TAG, "Error loading Rewarded Ad: ${e.message}")
        }
    }

    fun isRewardedAdReady(): Boolean = rewardedAd != null

    fun showRewarded(
        activity: Activity,
        onRewardEarned: (amount: Int, type: String) -> Unit,
        onAdClosed: () -> Unit = {}
    ) {
        val ad = rewardedAd
        if (ad != null) {
            var rewardGiven = false
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Rewarded Ad dismissed")
                    rewardedAd = null
                    loadRewarded(activity)
                    onAdClosed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.w(TAG, "Rewarded Ad failed to show: ${adError.message}")
                    rewardedAd = null
                    loadRewarded(activity)
                    onAdClosed()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Rewarded Ad showed")
                }
            }
            try {
                ad.show(activity) { rewardItem ->
                    rewardGiven = true
                    Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                    onRewardEarned(rewardItem.amount, rewardItem.type)
                }
            } catch (e: Exception) {
                Log.w(TAG, "Exception showing Rewarded Ad: ${e.message}")
                rewardedAd = null
                onAdClosed()
            }
        } else {
            loadRewarded(activity)
            onAdClosed()
        }
    }
}
