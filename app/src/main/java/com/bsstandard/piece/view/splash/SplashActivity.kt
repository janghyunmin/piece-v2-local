package com.bsstandard.piece.view.splash

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.AppVersionViewModel
import com.bsstandard.piece.databinding.ActivitySplashBinding
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.view.intro.IntroActivity
import com.bsstandard.piece.view.passcode.PassCodeActivity
import com.bsstandard.piece.widget.utils.DialogManager.openGoUpdate
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint

/**
 *packageName    : com.bsstandard.piece.view.splash
 * fileName       : SplashActivity
 * author         : piecejhm
 * date           : 2022/11/03
 * description    : Intro Splash Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/11/03        piecejhm       최초 생성
 */

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {


    private lateinit var vv: AppVersionViewModel
    var mContext: Context = this@SplashActivity
    var memberId: String = ""
    var localVersion: String = ""
    var storeVersion: String = ""

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        vv = ViewModelProvider(this@SplashActivity)[AppVersionViewModel::class.java]
        // 앱 버전 조회 ViewModel - jhm 2022/11/03
        binding.versionVm = vv
        binding.activity = this@SplashActivity
        binding.lifecycleOwner = this@SplashActivity


        setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
        setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
        setNaviBarIconColor(true) // 네비게이션 true : 검정색
        setNaviBarBgColor("#ffffff") // 네비게이션 배경색


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {

            } else {
                val intent = Intent(applicationContext, NetworkActivity::class.java)
                startActivity(intent)
            }
        }

        binding.splashAnimationView.setAnimation("splash1.json")
        binding.splashAnimationView.loop(false);
        binding.splashAnimationView.playAnimation()
        binding.splashAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                LogUtil.logE("start")
            }

            override fun onAnimationEnd(animation: Animator) {
                LogUtil.logE("end")
                versionChk()
            }

            override fun onAnimationCancel(animation: Animator) {
                LogUtil.logE("cancel")
            }

            override fun onAnimationRepeat(animation: Animator) {
                LogUtil.logE("repeat")
            }
        })

        // 현재 로컬 앱의 버전을 가져옴 - jhm 2022/06/13
        try {
            localVersion = packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        vv.getAppVersion("MDO0101")
        vv.detailResponse.observe(this@SplashActivity, Observer {
            PrefsHelper.write("appVersion",it.data.version)
            storeVersion = it.data.version
        })



    }

    // 앱 버전 비교  - jhm 2022/11/03
    private fun versionChk() {
        if (storeVersion > localVersion) {
            LogUtil.logE("업데이트 필요..")
            openGoUpdate(
                mContext,
                "업데이트 알림",
                "PIECE 앱을 최신버전으로 업데이트 해주세요.\n[확인]버튼을 누르시면 설치 화면으로 이동합니다",
                this
            )
        } else {
            LogUtil.logE("업데이트 불필요..")
            memberId = PrefsHelper.read("memberId", "")
            LogUtil.logE("memberId : $memberId")
            if (memberId == "") {
                val intent = Intent(mContext, IntroActivity::class.java)
                intent.putExtra("Step", "1") // 최초일때 - jhm 2022/10/17
                startActivity(intent)
            } else {
                val intent = Intent(mContext, PassCodeActivity::class.java)
                intent.putExtra("Step", "2") // 최초 로그인이 아닐때 - jhm 2022/10/17
                startActivity(intent)
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
//            val handler = Handler()
//            handler.postDelayed({
//
//            }, 1000)
        }
    }


    /** Util start **/
    /**
     * 상태바 아이콘 색상 지정
     * @param isBlack true : 검정색 / false : 흰색
     */
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
}