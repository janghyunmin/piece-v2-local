package com.bsstandard.piece.view.splash

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.RequiresApi
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

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        vv = ViewModelProvider(this@SplashActivity)[AppVersionViewModel::class.java]
        // 앱 버전 조회 ViewModel - jhm 2022/11/03
        binding.versionVm = vv
        binding.activity = this@SplashActivity
        binding.lifecycleOwner = this@SplashActivity



        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // Navigationbar 숨기기 - jhm 2022/11/04
        hideNavigationBar()

        // 상단 StatusBar 투명처리 - jhm 2022/11/04
        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            if (Build.VERSION.SDK_INT >= 19) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                if (Build.VERSION.SDK_INT < 21) {
                    setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
                } else {
                    setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
                    window.statusBarColor = Color.TRANSPARENT
                }
            }

        }


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {
                binding.splashAnimationView.setAnimation("splash1.json")
                binding.splashAnimationView.loop(false);
                binding.splashAnimationView.playAnimation()
                binding.splashAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        LogUtil.logE("SplashActivity Animation Start")
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        LogUtil.logE("SplashActivity Animation End")
                        versionChk()
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        LogUtil.logE("SplashActivity Animation Cancel")
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                        LogUtil.logE("SplashActivity Animation Repeat")
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
                    PrefsHelper.write("appVersion", it.data.version)
                    storeVersion = it.data.version
                })

            } else {
//                DialogManager.openNetWorkChk(this@SplashActivity)
                val intent = Intent(applicationContext, NetworkActivity::class.java)
                startActivity(intent)
            }
        }


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
        }
    }


    private fun setWindowFlag(bits: Int, on: Boolean) {
        val winAttr = window.attributes
        winAttr.flags = if (on) winAttr.flags or bits else winAttr.flags and bits.inv()
        window.attributes = winAttr
    }


    fun hideNavigationBar() {
        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
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