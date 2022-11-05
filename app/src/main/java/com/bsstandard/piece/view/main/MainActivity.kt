package com.bsstandard.piece.view.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.GetUserViewModel
import com.bsstandard.piece.databinding.ActivityMainBinding
import com.bsstandard.piece.view.common.LoginChkActivity
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.view.fragment.navigation.KeepStateNavigator
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.NetworkConnection
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 *packageName    : com.bsstandard.piece.ui.main
 * fileName       : MainActivity
 * author         : piecejhm
 * date           : 2022/05/02
 * description    : Dagger hilt 적용완료
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/02        piecejhm       최초 생성
 */


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val mainViewModel by viewModels<MainViewModel>()
    private val mv by viewModels<GetUserViewModel>()
    private lateinit var navController: NavController
    val manager = supportFragmentManager
    var mContext: Context = this@MainActivity
    var strSDFormatDay: String = ""


    @SuppressLint("NewApi", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.logE("Main onCreate..")

//        getStatusBarHeight(mContext)
//        binding.pLayout.setPadding(0,getStatusBarHeight(mContext),0,0)


//        supportActionBar?.hide()

        binding.apply {
            lifecycleOwner = this@MainActivity
            main = mainViewModel
            // 내정보 API - jhm 2022/09/03
//            memberVm = mv
//            mv.getUserData()


            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(false) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

            LogUtil.logE("accessToken Main : " + PrefsHelper.read("accessToken", ""));
            LogUtil.logE("deviceId Main : " + PrefsHelper.read("deviceId", ""));
            LogUtil.logE("expiredAt Main : " + PrefsHelper.read("expiredAt", ""));
            LogUtil.logE("memberId Main : " + PrefsHelper.read("memberId", ""));
            LogUtil.logE("refreshToken Main : " + PrefsHelper.read("refreshToken", ""));

            initNavController()


        }

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {

                initFirebase()
                updateResult()

            } else {
                val intent = Intent(applicationContext, NetworkActivity::class.java)
                startActivity(intent)
            }
        }


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        updateResult(true)
    }


    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                LogUtil.logE("token : ${task.result}")
            }
        }
    }

    private fun updateResult(isNewIntent: Boolean = false) {
        //true -> notification 으로 갱신된 것
        //false -> 아이콘 클릭으로 앱이 실행된 것
        (intent.getStringExtra("notificationType") ?: "앱 런처") + if (isNewIntent) {
            "(으)로 갱신했습니다."
        } else {
            "(으)로 실행했습니다."
        }
    }


    // BottomNavigation + Controller - jhm 2022/07/24
    // bottomNavigation fragment 재사용 방지 로직 추가 - jhm 2022/07/24
    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navMainFragment.id) as NavHostFragment
        val transaction: FragmentTransaction = manager.beginTransaction()
        var navController = navHostFragment.navController

        //Custom Navigator 추가
        val navigator = KeepStateNavigator(
            mContext,
            navHostFragment.childFragmentManager,
            binding.navMainFragment.id
        )

        navController.navigatorProvider += navigator
        // set navigation graph
        navController.setGraph(R.navigation.nav_graph)

        binding.bottomNav.setupWithNavController(navController)


    }

    private fun showBottomNav() {
        binding.bottomNav.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNav.visibility = View.GONE
    }


    // 로그인 여부에 따른 wallet , more 탭 분기 - jhm 2022/08/14
    @RequiresApi(Build.VERSION_CODES.O)
    fun onClick(item: MenuItem) {
        // Vibrator 객체
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator;

        var navHostFragment =
                supportFragmentManager.findFragmentById(binding.navMainFragment.id) as NavHostFragment
            try {
                val transaction: FragmentTransaction = manager.beginTransaction()

                when (item.itemId) {
                    R.id.FragmentHome -> {
                        var navController = navHostFragment.navController
                        navController.navigate(R.id.FragmentHome)
                        transaction.addToBackStack(null)
                        vibrator.vibrate(VibrationEffect.createOneShot(100,50))
                    }
                    R.id.FragmentMagazine -> {
                        var navController = navHostFragment.navController
                        navController.navigate(R.id.FragmentMagazine)
                        transaction.addToBackStack(null)
                        vibrator.vibrate(VibrationEffect.createOneShot(100,50))
                    }
                    R.id.FragmentWallet -> {
                        vibrator.vibrate(VibrationEffect.createOneShot(100,50))
                        LogUtil.logE("wallet..")
                        if (PrefsHelper.read("memberId", "").equals("")) {
                            val intent = Intent(this@MainActivity, LoginChkActivity::class.java)
                            startActivity(intent)
                        } else {
                            var navController = navHostFragment.navController
                            navController.navigate(R.id.FragmentWallet)
                            transaction.addToBackStack(null)
                        }
                    }
                    R.id.FragmentMore -> {
                        vibrator.vibrate(VibrationEffect.createOneShot(100,50))
                        LogUtil.logE("more..")
                        if (PrefsHelper.read("memberId", "").equals("")) {
                            val intent = Intent(this@MainActivity, LoginChkActivity::class.java)
                            startActivity(intent)
                        } else {
                            var navController = navHostFragment.navController
                            navController.navigate(R.id.FragmentMore)
                            transaction.addToBackStack(null)
                        }
                    }
                }
                transaction.setReorderingAllowed(true) // 화면전환간 애니메이션 정상 동작 처리 - jhm 2022/08/16
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                transaction.commitAllowingStateLoss()
            } catch (message: IllegalStateException) {
                LogUtil.logE("Exception : $message")
                message.printStackTrace()
            }
    }


    /** Util start **/
    /**
     * 상태바 아이콘 색상 지정
     * @param isBlack true : 검정색 / false : 흰색
     */

    private fun setStatusBar() {
        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
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


    // statusbar height 추가
    open fun getStatusBarHeight(context: Context): Int {
        val screenSizeType = context.resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK
        var statusbar = 0
        if (screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            val resourceId =
                context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusbar = context.resources.getDimensionPixelSize(resourceId)
            }
        }
        return statusbar
    }

    // statusbar height 추가
    fun getStatusBarHeightKt(context: Context): Int {
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

}
