package freelancerhelper.aab.data

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdManager {
    private const val TAG = "AdManager"
    const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false

    fun loadInterstitialAd(context: Context) {
        if ((interstitialAd != null) || isLoading) return
        isLoading = true
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context.applicationContext,
            TEST_INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                    Log.d(TAG, "Interstitial ad loaded successfully.")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                    Log.d(TAG, "Interstitial ad failed to load: ${loadAdError.message}")
                }
            },
        )
    }

    fun showInterstitialAd(activity: Activity?, onAdDismissedOrFailed: () -> Unit) {
        val ad = interstitialAd
        if ((activity != null) && (ad != null)) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterstitialAd(activity)
                    onAdDismissedOrFailed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.d(TAG, "Failed to show interstitial ad: ${adError.message}")
                    interstitialAd = null
                    loadInterstitialAd(activity)
                    onAdDismissedOrFailed()
                }
            }
            ad.show(activity)
        } else {
            activity?.let { loadInterstitialAd(it) }
            onAdDismissedOrFailed()
        }
    }
}
