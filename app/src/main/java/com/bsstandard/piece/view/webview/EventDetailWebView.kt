package com.bsstandard.piece.view.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.viewmodel.EventDetailViewModel
import com.bsstandard.piece.databinding.ActivityEventWebviewBinding
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

/**
 *packageName    : com.bsstandard.piece.view.webview
 * fileName       : EventDetailWebView
 * author         : piecejhm
 * date           : 2022/09/11
 * description    : 이벤트 상세 WebView Class
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/11        piecejhm       최초 생성
 */

@AndroidEntryPoint
class EventDetailWebView :
    BaseActivity<ActivityEventWebviewBinding>(R.layout.activity_event_webview) {
    private var disposable: Disposable? = null
    private lateinit var webView: WebView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var vm: EventDetailViewModel
    private var eventId: String = ""
    private var url: String = ""

    @SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@EventDetailWebView
            binding.eventDetailWebView = this@EventDetailWebView
            binding.eventDetailViewModel = eventDetailViewModel

            vm = ViewModelProvider(this@EventDetailWebView)[EventDetailViewModel::class.java]

            setStatusBar()
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#00000000") // 상태바 배경색상 설정
            postponeEnterTransition()


            eventId = intent.getStringExtra("eventId").toString()
            vm.getEventDetail(eventId)
            vm.detailResponse.observe(this@EventDetailWebView, Observer {
                LogUtil.logE("event response : " + it.data.contents)
                url = it.data.contents

                binding.webView.apply {
                    webViewClient = WebViewClient() // 새창안뜨게 - jhm 2022/08/30
                    settings.javaScriptEnabled = true // 자바스크립트 허용 - jhm 2022/08/30
                    settings.setSupportMultipleWindows(false) // 새창 띄우기 허용 여부 - jhm 2022/08/30
                    settings.javaScriptCanOpenWindowsAutomatically =
                        false // 자바스크립트 새창 띄우기 (멀티뷰) 허용 여부 - jhm 2022/08/30
                    settings.loadWithOverviewMode = true // 메타태그 허용 - jhm 2022/08/30
                    settings.useWideViewPort = true // 화면 사이즈 맞추기 허용 - jhm 2022/08/30
                    settings.setSupportZoom(false) // 화면 줌 허용 여부 - j부m 2022/08/30
                    settings.builtInZoomControls = false // 화면 확대 축소 허용 여부 - jhm 2022/08/30

                    settings.cacheMode = WebSettings.LOAD_NO_CACHE // 브라우저 캐시 허용 여부 - jhm 2022/08/30
                    settings.domStorageEnabled = true // 로컬 저장소 허용 여부 - jhm 2022/08/30
                    settings.displayZoomControls = false // 줌 컨트롤 허용 여부 - jhm 2022/08/30

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        settings.safeBrowsingEnabled = true // api 26 - jhm 2022/08/30
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        settings.mediaPlaybackRequiresUserGesture = false
                    }
                    settings.allowContentAccess = true
                    settings.setGeolocationEnabled(false) // 위치 허용 여부 - jhm 2022/08/30
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        settings.allowUniversalAccessFromFileURLs = true
                    }
                    settings.allowFileAccess = true
                    binding.webView.loadDataWithBaseURL(
                        null,
                        it.data.contents,
                        "text/html; charset=utf-8",
                        "utf-8",
                        null
                    )
                }

                // 뒤로가기 - jhm 2022/09/11
                binding.backImg.setOnClickListener {
                    LogUtil.logE("webview 종료")
                    finish()
                }

                // 공유하기 - jhm 2022/09/11
                binding.shareImg.setOnClickListener {
                    val eventId = eventId
                    val shareUrl = vm.detailResponse.value?.data?.shareUrl
                    val eventTitle = vm.detailResponse.value?.data?.title

                    //공유 가능한 앱들을 실행하는 Action값으로 intent생성
                    val sendIntent: Intent = Intent().apply {

                        action = Intent.ACTION_SEND

                        putExtra(Intent.EXTRA_SUBJECT, eventTitle)
                        putExtra(Intent.EXTRA_TEXT, shareUrl)
//                        putExtra(Intent.EXTRA_STREAM, uri)

                        type = "text/plain" //type은 image, text, video등이 존재

                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }
            })


        }
    }

    // 웹뷰 새창이 아닌 기존창에서 실행되도록 - jhm 2022/08/30
    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request != null) {
                view?.loadUrl(request.url.toString())
            }
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            super.onReceivedSslError(view, handler, error)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        LogUtil.logE("EventDetailWebView onStart..")
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.let { disposable!!.dispose() }
    }


    /** Util start **/
    /**
     * 상태바 아이콘 색상 지정
     * @param isBlack true : 검정색 / false : 흰색
     */

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = 0x00000000  // transparent
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.addFlags(flags)
        }
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    private fun setStatusBarIconColor(isBlack: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // android os 12에서 사용 가능

            window.insetsController?.let {
                it.setSystemBarsAppearance(
                    if (isBlack) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // minSdk 6.0부터 사용 가능
            window.decorView.systemUiVisibility = if (isBlack) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                // 기존 uiVisibility 유지
                window.decorView.systemUiVisibility
            }

        } // end if

    }

    /**
     * 상태바 배경 색상 지정
     * @param colorHexValue #ffffff 색상 값
     */
    private fun setStatusBarBgColor(colorHexValue: String) {

        // 상태바 배경색은 5.0부터 가능하나, 아이콘 색상은 6.0부터 변경 가능
        // -> 아이콘/배경색 모두 바뀌어야 의미가 있으므로 6.0으로 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = Color.parseColor(colorHexValue)

        } // end if

    }

    /**
     * 내비바 아이콘 색상 지정
     * @param isBlack true : 검정색 / false : 흰색
     */
    private fun setNaviBarIconColor(isBlack: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // android os 12에서 사용 가능

            window.insetsController?.let {
                it.setSystemBarsAppearance(
                    if (isBlack) WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 내비바 아이콘 색상이 8.0부터 가능하므로 커스텀은 동시에 진행해야 하므로 조건 동일 처리.
            window.decorView.systemUiVisibility =
                if (isBlack) {
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

                } else {
                    // 기존 uiVisibility 유지
                    // -> 0으로 설정할 경우, 상태바 아이콘 색상 설정 등이 지워지기 때문
                    window.decorView.systemUiVisibility

                } // end if

        } // end if
    }

    /**
     * 내비바 배경 색상 설정
     * @param colorHexValue #ffffff 색상 값
     */
    private fun setNaviBarBgColor(colorHexValue: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 내비바 배경색은 8.0부터 지원한다.
            window.navigationBarColor = Color.parseColor(colorHexValue)

        } // end if

    }
    /** Util end **/


}