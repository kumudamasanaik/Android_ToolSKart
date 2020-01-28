package com.toolskart.android.ui.aboutus

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.*
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.constants.Constants
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.model.AboutUsRes
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import com.toolskart.android.utils.showToastMsg
import kotlinx.android.synthetic.main.activity_about_us.*

class AboutUsActivity : BaseActivity(), AboutUsContract.View {
    private lateinit var mResult: AboutUsRes
    private val TAG = "AboutUsActivity"
    private lateinit var mContext: Context
    private lateinit var presenter: AboutUsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_about_us, fragmentLayout)
        mContext = this
        init()
    }

    override fun init() {
        presenter = AboutUsPresenter(this)
        tvTitle?.text = getString(R.string.about_us)
        hideCartView()
        callAboutUsScreenApi()
    }

    override fun callAboutUsScreenApi() {
        if (LiveNetworkMonitor(this).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callAboutUsAPi()
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setAboutUsApiRes(response: AboutUsRes) {
        if (Validation.isValidObject(response)) {
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            mResult = response
            setupWebView()
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    private fun setupWebView() {
        webview.webViewClient = MyWebViewClient()
        webview.clearCache(true)
        webview.clearHistory()
        webview.settings.javaScriptEnabled = true
        webview.settings.javaScriptCanOpenWindowsAutomatically = false
        webview.settings.setSupportMultipleWindows(false)
        webview.settings.setSupportZoom(false)
        webview.settings.domStorageEnabled = true
        webview.isVerticalScrollBarEnabled = false
        webview.isHorizontalScrollBarEnabled = false
        webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress >= 95)
                    hideLoader()
                else
                    showLoader()
            }

            override fun onReceivedTitle(view: WebView, title: String) {
            }
        }

        if (Validation.isValidObject(mResult.result) && Validation.isValidString(mResult.result!!.aboutus!![0].name)) {
            webview.loadUrl("${Constants.pdfLoadUrl}${mResult.result!!.aboutus!![0].name}")
        }
    }

    private inner class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onReceivedError(webView: WebView, reques: WebResourceRequest, error: WebResourceError) {
            super.onReceivedError(webView, reques, error)
            hideLoader()
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            showLoader()
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            hideLoader()
        }
    }

    override fun showMsg(msg: String?) {
        showToastMsg(msg!!)
    }

    override fun showLoader() {
        CommonUtils.showLoading(this, "", true)
    }

    override fun hideLoader() {
        CommonUtils.hideLoading()
    }

    override fun showViewState(state: Int) {

    }
}