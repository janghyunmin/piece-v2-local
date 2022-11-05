package com.bsstandard.piece.view.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datamodel.dmodel.magazine.MemberBookmarkRegModel
import com.bsstandard.piece.data.datamodel.dmodel.magazine.MemberBookmarkRemoveModel
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.BaseDTO
import com.bsstandard.piece.data.viewmodel.MagazineDetailViewModel
import com.bsstandard.piece.data.viewmodel.MemberMagazineDetailViewModel
import com.bsstandard.piece.databinding.ActivityMagazineWebviewBinding
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.view.common.LoginChkActivity
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.view.webview.dialog.MagazineBottomSheetDialog
import com.bsstandard.piece.widget.extension.SnackBarCustom
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.NetworkConnection
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 *packageName    : com.bsstandard.piece.view.webview
 * fileName       : MagazineDetailWebView
 * author         : piecejhm
 * date           : 2022/08/30
 * description    : 매거진(라운지) 상세 WebView Class
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/30        piecejhm       최초 생성
 */

@AndroidEntryPoint
class MagazineDetailWebView :
    BaseActivity<ActivityMagazineWebviewBinding>(R.layout.activity_magazine_webview) {

    private var disposable: Disposable? = null
    private lateinit var webView: WebView
    private lateinit var vm: MagazineDetailViewModel
    private lateinit var mvm: MemberMagazineDetailViewModel
    private var magazineId: String = ""
    private var isFavorite: String = ""
    private var position: Int = 0
    private var url: String = ""
    private var shareUrl: String = ""

    private val TAG = this.javaClass.simpleName
    val response: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    // 기존 북마크 위치 아이콘 클릭시 BottomSheet- jhm 2022/11/01
    private var magazineBottomSheetDialog: MagazineBottomSheetDialog? = null

    @SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.apply {
            lifecycleOwner = this@MagazineDetailWebView
            binding.magazineDetailWebView = this@MagazineDetailWebView
            binding.magazineDetailViewModel = magazineDetailViewModel

            vm = ViewModelProvider(this@MagazineDetailWebView)[MagazineDetailViewModel::class.java]
            mvm =
                ViewModelProvider(this@MagazineDetailWebView)[MemberMagazineDetailViewModel::class.java]
            setStatusBar()
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#00000000") // 상태바 배경색상 설정

            val networkConnection = NetworkConnection(applicationContext)
            networkConnection.observe(lifecycleOwner as MagazineDetailWebView) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
                if (isConnected) {
                    postponeEnterTransition()
                    magazineId = intent.getStringExtra("magazineId").toString()
                    webViewCallback()


                    // 비회원으로 매거진 들어왔을때 - jhm 2022/08/30
                    if (PrefsHelper.read("memberId", "").equals("")) {
                        vm.getNoMemberMagazineDetail(magazineId)
                        vm.detailResponse.observe(this@MagazineDetailWebView, Observer {
                            shareUrl = it.data.shareUrl
                            url = it.data.contents

                            binding.title.text = it.data.title
                            binding.midTitle.text = it.data.smallTitle + " | " + it.data.createdAt

                            binding.webView.loadDataWithBaseURL(
                                null,
                                it.data.contents,
                                "text/html; charset=utf-8",
                                "utf-8",
                                null
                            )
                        })
                    } else {
                        mvm.getMemberMagazineDetail(magazineId)
                        mvm.detailResponse.observe(this@MagazineDetailWebView, Observer {
                            shareUrl = it.data.shareUrl
                            url = it.data.contents

                            binding.title.text = it.data.title
                            binding.midTitle.text = it.data.smallTitle + " | " + it.data.createdAt

                            binding.webView.loadDataWithBaseURL(
                                null,
                                it.data.contents,
                                "text/html; charset=utf-8",
                                "utf-8",
                                null
                            )

                        })

                        mvm.isFavorite.observe(this@MagazineDetailWebView, Observer {
                            isFavorite = it.toString()
                            LogUtil.logE("회원 전용 isFavorite $isFavorite")
                        })
                    }


                    binding.backImg.setOnClickListener {
                        LogUtil.logE("webview 종료")
                        finish()
                    }


                    // 북마크 저장에서 변경 - jhm 2022/11/01
                    binding.option.setOnClickListener {
                        LogUtil.logE("상세 북마크 클릭..")
                        val bundle = Bundle()
                        bundle.putString("shareUrl", shareUrl) // 공유되는 주소 - jhm 2022/10/21
                        bundle.putString("magazineId", magazineId)
                        bundle.putString("isFavorite", isFavorite) // 저장 판별 - jhm 2022/11/01

                        val dialog = MagazineBottomSheetDialog(
                            this@MagazineDetailWebView,
                            R.layout.slideup_shared
                        )
                        dialog.apply {
                            showDialog(shareUrl, magazineId, isFavorite)
                            setCallback(object :
                                MagazineBottomSheetDialog.OnSendFromBottomSheetDialog {
                                override fun sendValue(value: String, boolean: Boolean) {
                                    LogUtil.logE("액티비티로 전달된 값 $value")
                                    when (value) {
                                        "공유" -> {

                                        }
                                        "링크 복사" -> {
                                            val drawable = ContextCompat.getDrawable(
                                                context,
                                                R.drawable.clip_copy_icon
                                            )!!
                                            SnackBarCustom.make(
                                                binding.root,
                                                "링크를 클립보드에 복사했습니다.",
                                                drawable
                                            ).show()
                                        }
                                        "북마크" -> {
                                            LogUtil.logE("boolean : $boolean")
                                            // 북마크 클릭시 로그인 판별 - jhm 2022/11/01
                                            if (PrefsHelper.read("memberId", "").equals("")) {
                                                val intent =
                                                    Intent(
                                                        this@MagazineDetailWebView,
                                                        LoginChkActivity::class.java
                                                    )
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                this@MagazineDetailWebView.startActivity(intent)
                                                overridePendingTransition(
                                                    android.R.anim.fade_in,
                                                    android.R.anim.fade_out
                                                );
                                            } else {
                                                // 로그인이 되어있다면 - jhm 2022/11/01
                                                // 북마크 Post 요청시 필요 모델 - jhm 2022/08/29
                                                val memberBookmarkRegModel = MemberBookmarkRegModel(
                                                    PrefsHelper.read("memberId", ""),
                                                    magazineId
                                                )
                                                // 북마크 Delete 요청시 필요 모델 - jhm 2022/08/29
                                                val memberBookmarkRemoveModel =
                                                    MemberBookmarkRemoveModel(
                                                        PrefsHelper.read(
                                                            "memberId",
                                                            ""
                                                        ), magazineId
                                                    )

                                                if (!boolean) {
                                                    LogUtil.logE("북마크 저장")
                                                    response?.updateBookMark(
                                                        "Bearer $accessToken",
                                                        deviceId,
                                                        memberId,
                                                        memberBookmarkRegModel
                                                    )?.enqueue(object : Callback<BaseDTO> {
                                                        override fun onResponse(
                                                            call: Call<BaseDTO>,
                                                            response: Response<BaseDTO>
                                                        ) {
                                                            LogUtil.logE("bookmark post call success.." + response.body()?.status)
                                                            try {
                                                                if (!response.body().toString()
                                                                        .isEmpty()
                                                                ) {
                                                                    LogUtil.logE("bookmark status : " + response.body()?.status)
                                                                    mvm.getMemberMagazineDetail(
                                                                        magazineId
                                                                    )

                                                                    val drawable =
                                                                        ContextCompat.getDrawable(
                                                                            context,
                                                                            R.drawable.bm_snack_bk_on_icon
                                                                        )!!
                                                                    SnackBarCustom.make(
                                                                        binding.root,
                                                                        "컬렉션에 저장 되었습니다.",
                                                                        drawable
                                                                    ).show()
                                                                }
                                                            } catch (ex: Exception) {
                                                                ex.printStackTrace()
                                                                LogUtil.logE("북마크 POST Error..! ${ex.message}")
                                                            }
                                                        }

                                                        override fun onFailure(
                                                            call: Call<BaseDTO>,
                                                            t: Throwable
                                                        ) {
                                                            t.printStackTrace()
                                                            LogUtil.logE("bookmark post call fail.." + t.stackTrace)
                                                        }
                                                    })

                                                    onResume()


                                                } else {
                                                    LogUtil.logE("북마크 취소")

                                                    response?.deleteBookMark(
                                                        "Bearer $accessToken",
                                                        deviceId,
                                                        memberId,
                                                        memberBookmarkRemoveModel
                                                    )
                                                        ?.enqueue(object : Callback<BaseDTO> {
                                                            @SuppressLint("NotifyDataSetChanged")
                                                            override fun onResponse(
                                                                call: Call<BaseDTO>,
                                                                response: Response<BaseDTO>
                                                            ) {
                                                                LogUtil.logE("bookmark delete call success.." + response.body()?.status)
                                                                try {
                                                                    if (!response.body().toString()
                                                                            .isEmpty()
                                                                    ) {
                                                                        LogUtil.logE("bookmark status : " + response.body()?.status)
                                                                        mvm.getMemberMagazineDetail(
                                                                            magazineId
                                                                        )
                                                                    }
                                                                } catch (ex: Exception) {
                                                                    ex.printStackTrace()
                                                                }
                                                            }

                                                            override fun onFailure(
                                                                call: Call<BaseDTO>,
                                                                t: Throwable
                                                            ) {
                                                                LogUtil.logE("bookmark post call fail.." + t.stackTrace)

                                                            }
                                                        })
                                                    onResume()
                                                }
                                            }
                                        }
                                    }
                                }
                            })
                        }
                    }
                } else {
                    val intent = Intent(applicationContext, NetworkActivity::class.java)
                    startActivity(intent)
                }
            }


        }
    }

    private fun webViewCallback() {
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

        }
    }

    private fun createSnackBar(message: String, duration: Int, type: String): Snackbar {
        return Snackbar.make(binding.root, message, duration)
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
            binding.progressBar.show()
        }
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.progressBar.hide()
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
        LogUtil.logE("PortfolioDetailActivity onStart..")
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.let { disposable!!.dispose() }
    }

    override fun onResume() {
        super.onResume()
    }


    // extention.kt
    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    fun getNaviBarHeight(context: Context): Int {
        val resourceId: Int =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /** Util start **/
    /**
     * 상태바 아이콘 색상 지정
     * @param isBlack true : 검정색 / false : 흰색
     */

    private fun setStatusBar() {
//        val w: Window = window
//        w.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
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


//    open fun getStatusBarHeight(context: Context): Int {
//        val screenSizeType: Int = context.getResources().getConfiguration().screenLayout and
//                Configuration.SCREENLAYOUT_SIZE_MASK
//        var statusbar = 0
//        if (screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
//            val resourceId: Int =
//                context.getResources().getIdentifier("status_bar_height", "dimen", "android")
//            if (resourceId > 0) {
//                statusbar = context.getResources().getDimensionPixelSize(resourceId)
//            }
//        }
//        return statusbar
//    }
}