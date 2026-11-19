package com.spectrumonereverse.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val baseUrl = "https://spectrumonereverse.com/"
    private val phone = "8888135655"
    private lateinit var web: WebView
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var progress: ProgressBar
    private lateinit var offline: TextView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        web = findViewById(R.id.webview)
        swipe = findViewById(R.id.swipe)
        progress = findViewById(R.id.progress)
        offline = findViewById(R.id.offline)

        web.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = true
            setSupportZoom(false)
        }

        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(v: WebView, req: WebResourceRequest): Boolean {
                val uri = req.url
                val scheme = uri.scheme ?: return false
                return when {
                    scheme == "tel" || scheme == "mailto" -> { open(uri); true }
                    uri.host?.endsWith("spectrumonereverse.com") == true -> false
                    else -> { open(uri); true }   // external sites browser me khulenge
                }
            }
            override fun onPageStarted(v: WebView, url: String?, icon: Bitmap?) {
                progress.visibility = View.VISIBLE
                offline.visibility = if (isOnline()) View.GONE else View.VISIBLE
            }
            override fun onPageFinished(v: WebView, url: String?) {
                progress.visibility = View.GONE
                swipe.isRefreshing = false
            }
        }
        web.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(v: WebView, p: Int) { progress.progress = p }
        }

        swipe.setOnRefreshListener { web.reload() }

        findViewById<BottomNavigationView>(R.id.bottom_nav).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> web.loadUrl(baseUrl)
                R.id.nav_calculator -> web.loadUrl(baseUrl + "calculator/")
                R.id.nav_rates -> web.loadUrl(baseUrl + "rates/")
                R.id.nav_call -> open(Uri.parse("tel:$phone"))
            }
            it.itemId != R.id.nav_call
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (web.canGoBack()) web.goBack() else { isEnabled = false; onBackPressedDispatcher.onBackPressed() }
            }
        })

        if (savedInstanceState == null) web.loadUrl(baseUrl) else web.restoreState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState); web.saveState(outState)
    }

    private fun open(uri: Uri) {
        val action = if (uri.scheme == "tel") Intent.ACTION_DIAL else Intent.ACTION_VIEW
        try { startActivity(Intent(action, uri)) } catch (_: Exception) {}
    }

    @Suppress("DEPRECATION")
    private fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo?.isConnected == true
    }
}
