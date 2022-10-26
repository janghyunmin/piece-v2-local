package com.bsstandard.piece.view.myInfo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.GetUserViewModel
import com.bsstandard.piece.databinding.ActivityMyinfoBinding
import com.bsstandard.piece.view.join.JoinActivity
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint

/**
 *packageName    : com.bsstandard.piece.view.myInfo
 * fileName       : MyInfoActivity
 * author         : piecejhm
 * date           : 2022/09/01
 * description    : 내정보 상세 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/01        piecejhm       최초 생성
 */

@AndroidEntryPoint
class MyInfoActivity : BaseActivity<ActivityMyinfoBinding>(R.layout.activity_myinfo) {

    private val myinfoBinding by lazy { ActivityMyinfoBinding.inflate(layoutInflater) }
    private val mv by viewModels<GetUserViewModel>()
    var myinfoBottomSheetDialog: MyInfoBottomSheetDialog? = null
    var myInfoEmailDialog: MyInfoEmailDialog? = null
    var mContext: Context = this@MyInfoActivity

    private var liveAddress: MutableLiveData<String> = MutableLiveData()

    override fun onStart() {
        super.onStart()
        LogUtil.logE("onStart ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myinfoBottomSheetDialog = MyInfoBottomSheetDialog(mContext)
        myInfoEmailDialog = MyInfoEmailDialog(mContext)


        binding.apply {
            LogUtil.logE("여기 시작")
            lifecycleOwner = this@MyInfoActivity
            myinfoBinding.lifecycleOwner = this@MyInfoActivity
            myinfoBinding.activity = this@MyInfoActivity

            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

            // 내정보 API - jhm 2022/09/03
            memberVm = mv

            // 유저 재인증 버튼 - jhm 2022/09/04
            binding.reAuth.setOnClickListener { view ->
                piece_reauth()
                LogUtil.logE("유저 재인증 OnClick..")
            }

            mv.loadData()
            mv.liveAddress.observe(this@MyInfoActivity, Observer {
                LogUtil.logE("옵저버 1 : $it")
                binding.baseAddress.text = it.toString()

            })
            // 사용자가 입력한 상세 주소 - jhm 2022/09/08
            mv.liveDetailAddress.observe(this@MyInfoActivity, Observer {
                LogUtil.logE("옵저버 2 : $it")
                binding.detailAddress.text = it.toString()
            })



            binding.userName.text = PrefsHelper.read("name", "")
            binding.userBirth.text = PrefsHelper.read("birthDay", "")
            binding.userPhone.text = PrefsHelper.read("cellPhoneNo", "")


            binding.address.setOnClickListener { view ->
                LogUtil.logE("주소 변경 OnClick..")
                myinfoBottomSheetDialog!!.show(supportFragmentManager, "주소변경")
            }

            binding.emailChange.setOnClickListener { view ->
                LogUtil.logE("이메일 변경 OnClick.")
                myInfoEmailDialog!!.show(supportFragmentManager,"이메일 변경")
            }

            // 등록되어있는 주소가 없거나 , 있을때 View Change - jhm 2022/09/08
            if (PrefsHelper.read("baseAddress", "").isEmpty()) {
                LogUtil.logE("여기1")
                binding.noAddress.visibility = View.VISIBLE
                binding.baseAddress.visibility = View.GONE
                binding.detailAddress.visibility = View.GONE
            } else {
                LogUtil.logE("여기2")
                binding.noAddress.visibility = View.GONE
                binding.baseAddress.visibility = View.VISIBLE
                binding.detailAddress.visibility = View.VISIBLE
            }

            // 등록되어있는 이메일 주소 없거나 , 있을때 View Change - jhm 2022/10/17
            if (PrefsHelper.read("email", "").isEmpty()) {
                binding.noEmail.visibility = View.VISIBLE
                binding.email.visibility = View.GONE
            } else {
                binding.email.text = PrefsHelper.read("email","").toString()
                binding.noEmail.visibility = View.GONE
                binding.email.visibility = View.VISIBLE
            }

            binding.email.text = PrefsHelper.read("email","").toString()
        }
    }


    // 유저 재인증 - jhm 2022/09/03
    fun piece_reauth() {
        val intent = Intent(this, JoinActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        LogUtil.logE("onResume...")
        mv.getUserData()
        mv.loadData()

        mv.liveAddress.observe(this@MyInfoActivity, Observer {
            LogUtil.logE("baseAddress : $it")
            binding.baseAddress.text = it.toString()
        })
        // 사용자가 입력한 상세 주소 - jhm 2022/09/08
        mv.liveDetailAddress.observe(this@MyInfoActivity, Observer {
            LogUtil.logE("detailAddress : $it")
            binding.detailAddress.text = it.toString()
        })
    }

    override fun onPause() {
        super.onPause()
        LogUtil.logE("onPause")
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