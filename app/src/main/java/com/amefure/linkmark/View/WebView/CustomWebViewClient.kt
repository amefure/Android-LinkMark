package com.amefure.linkmark.View.WebView

import android.webkit.WebView
import android.webkit.WebViewClient

class CustomWebViewClient : WebViewClient() {
    // カスタムクライアント(ブラウザを起動せずにアプリ内で画面遷移を許可)
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        return false
    }
}