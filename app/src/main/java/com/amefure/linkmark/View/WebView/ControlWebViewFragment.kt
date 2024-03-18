package com.amefure.linkmark.View.WebView

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.webkit.WebView
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.amefure.linkmark.Model.Key.AppArgKey
import com.amefure.linkmark.R

class ControlWebViewFragment : Fragment() {

    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(AppArgKey.ARG_URL_KEY).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_control_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView: WebView = view.findViewById(R.id.web_view)

        val backButton: ImageButton = view.findViewById(R.id.back_button)
        val forwardButton: ImageButton = view.findViewById(R.id.forward_button)
        val shareButton: ImageButton = view.findViewById(R.id.share_button)
        val browserButton: ImageButton = view.findViewById(R.id.browser_button)
        val reloadButton: ImageButton = view.findViewById(R.id.reload_button)

        setUpHeaderAction(view)

        backButton.setOnClickListener {
            webView.goBack()
        }

        forwardButton.setOnClickListener {
            webView.goForward()
        }

        browserButton.setOnClickListener {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        reloadButton.setOnClickListener {
            webView.reload()
        }


        // カスタムクライアント(ブラウザを起動せずにアプリ内で画面遷移を許可)
        val client = CustomWebViewClient()
        webView.webViewClient = client
        // ロードするURLが無効な場合に念の為備える
        webView.loadUrl(url.takeIf { URLUtil.isValidUrl(it) } ?: "https://appdev-room.com/")
    }

    /**
     * ヘッダーボタンセットアップ
     * [LeftButton]：backButton
     * [RightButton]：非表示(GONE)
     */
    private fun setUpHeaderAction(view: View) {
        val headerView: ConstraintLayout = view.findViewById(R.id.include_header)

        val leftButton: ImageButton = headerView.findViewById(R.id.left_button)
        leftButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val rightButton: ImageButton = headerView.findViewById(R.id.right_button)
        rightButton.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String): ControlWebViewFragment
        = ControlWebViewFragment().apply {
            arguments = Bundle().apply {
                putString(AppArgKey.ARG_URL_KEY, url)
            }
        }
    }
}