package com.demo.negative.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.demo.negative.R

class NegativeScreenService : Service() {

    companion object {
        private const val TAG = "NegativeScreenService"
        // 默认加载的 H5 页面（本地 assets）
        private const val DEFAULT_URL = "file:///android_asset/index.html"
    }

    private var webView: WebView? = null
    private var windowManager: WindowManager? = null
    private var rootLayout: FrameLayout? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        createNegativeScreenView()
    }

    private fun createNegativeScreenView() {
        // 创建根布局
        rootLayout = FrameLayout(this).apply {
            setBackgroundColor(0xFFFFFFFF.toInt())
        }

        // 创建 WebView
        webView = WebView(this).apply {
            setBackgroundColor(0xFFFFFFFF.toInt())
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }

        // 配置 WebView
        setupWebView(webView!!)

        // 添加到根布局
        rootLayout!!.addView(webView)

        // 创建布局参数
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        }

        // 添加到窗口管理器
        try {
            windowManager?.addView(rootLayout, layoutParams)
            Log.d(TAG, "View added to window manager")

            // 加载页面
            webView?.loadUrl(DEFAULT_URL)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add view", e)
        }
    }

    private fun setupWebView(webView: WebView) {
        webView.settings.apply {
            // 启用 JavaScript
            javaScriptEnabled = true
            // 启用 DOM 存储
            domStorageEnabled = true
            // 启用数据库
            databaseEnabled = true
            // 缓存模式
            cacheMode = WebSettings.LOAD_DEFAULT
            // 视口
            useWideViewPort = true
            loadWithOverviewMode = true
            // 禁用缩放
            setSupportZoom(false)
            builtInZoomControls = false
            displayZoomControls = false
            // HTTPS 支持
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            // 允许文件访问
            allowFileAccess = true
            allowContentAccess = true
            // 性能优化
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        }

        // WebViewClient
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                // 不拦截内部链接
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                Log.d(TAG, "Page started: $url")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d(TAG, "Page finished: $url")
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Log.e(TAG, "Page error: ${error?.description}")
            }
        }

        // WebChromeClient
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                Log.d(TAG, "Progress: $newProgress%")
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                Log.d(TAG, "Title: $title")
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.d(TAG, "Service destroyed")
        // 移除 WebView
        try {
            rootLayout?.let {
                windowManager?.removeView(it)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to remove view", e)
        }
        webView?.destroy()
        webView = null
        rootLayout = null
        super.onDestroy()
    }
}