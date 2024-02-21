package com.amefure.linkmark.View.WebView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.webkit.WebView
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

        val myWebView: WebView = view.findViewById(R.id.web_view)
        // ロードするURLが無効な場合に念の為備える
        myWebView.loadUrl(url.takeIf { URLUtil.isValidUrl(it) } ?: "https://appdev-room.com/")
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