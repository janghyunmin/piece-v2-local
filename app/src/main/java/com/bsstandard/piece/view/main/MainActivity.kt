package com.bsstandard.piece.view.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.bsstandard.piece.databinding.ActivityMainBinding
import com.bsstandard.piece.view.common.LoginChkActivity
import com.bsstandard.piece.view.fragment.navigation.KeepStateNavigator
import com.bsstandard.piece.view.main.dialog.EventSheet
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    private lateinit var navController: NavController
    val manager = supportFragmentManager
    var mContext: Context = this@MainActivity


    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.logE("Main onCreate..")

        binding.apply {
            lifecycleOwner = this@MainActivity
            main = mainViewModel

            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

            PrefsHelper.init(this@MainActivity)
            LogUtil.logE("accessToken Main : " + PrefsHelper.read("accessToken", ""));
            LogUtil.logE("deviceId Main : " + PrefsHelper.read("deviceId", ""));
            LogUtil.logE("expiredAt Main : " + PrefsHelper.read("expiredAt", ""));
            LogUtil.logE("memberId Main : " + PrefsHelper.read("memberId", ""));
            LogUtil.logE("refreshToken Main : " + PrefsHelper.read("refreshToken", ""));

            initNavController()
            
            //toDayShow() // 메인 이벤트 바텀 시트 - jhm 2022/08/31
        }
    }

    // 오늘은 보지 않기 눌렀는지 chk - jhm 2022/07/08
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDayShow() {
        val eventSheet = EventSheet()

        val toDayChk = PrefsHelper.read("toDayChk", "false")
        if (toDayChk.equals("true")) {
            toDayChk()
            println("오늘은 보지않기 눌러서 오늘 못봄..")
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                eventSheet.show(supportFragmentManager, eventSheet.tag)
            }, 500)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toDayChk() {
        // 자정시간 - jhm 2022/07/07
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시", Locale("ko", "KR"))
        val midNight = dateFormat.format(Date(calendar.timeInMillis))
        println("midNight : $midNight")

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시")
        val today = current.format(formatter)
        println("today : $today")

        val compare = today.compareTo(midNight)
        println("compare : $compare")

        when {
            compare > 0 -> {
                println("after")
                PrefsHelper.write("toDayChk", "false")
            }
            compare < 0 -> {
                println("before")
                PrefsHelper.write("toDayChk", "true")
            }
//            else -> {
//                println("equals")
//                PrefsHelper.write("toDayChk", "false")
//            }
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

        LogUtil.logE("id : " + binding.navMainFragment.id)

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
    fun onClick(item: MenuItem) {
        var navHostFragment =
            supportFragmentManager.findFragmentById(binding.navMainFragment.id) as NavHostFragment
        val transaction: FragmentTransaction = manager.beginTransaction()


        when (item.itemId) {
            R.id.FragmentHome -> {
                var navController = navHostFragment.navController
                navController.navigate(R.id.FragmentHome)
                transaction.addToBackStack(null)
            }
            R.id.FragmentMagazine -> {
                var navController = navHostFragment.navController
                navController.navigate(R.id.FragmentMagazine)
                transaction.addToBackStack(null)
            }
            R.id.FragmentWallet -> {
                LogUtil.logE("wallet..")
                if (!PrefsHelper.read("isJoin", "").equals("true")) {
                    val intent = Intent(this@MainActivity, LoginChkActivity::class.java)
                    startActivity(intent)
                } else {
                    var navController = navHostFragment.navController
                    navController.navigate(R.id.FragmentWallet)
                    transaction.addToBackStack(null)
                }
            }
            R.id.FragmentMore -> {
                LogUtil.logE("more..")
                if (!PrefsHelper.read("isJoin", "").equals("true")) {
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
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
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


    /** Util end **/

    private fun setStatusBar(){
        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

}
