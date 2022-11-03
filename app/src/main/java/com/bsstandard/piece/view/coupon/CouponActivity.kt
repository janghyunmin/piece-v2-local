package com.bsstandard.piece.view.coupon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.viewmodel.CouponViewModel
import com.bsstandard.piece.databinding.ActivityCouponBinding
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.widget.utils.CustomDialogListener
import com.bsstandard.piece.widget.utils.DialogManager
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

/**
 *packageName    : com.bsstandard.piece.view.coupon
 * fileName       : CouponActivity
 * author         : piecejhm
 * date           : 2022/09/13
 * description    : 쿠폰함 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/13        piecejhm       최초 생성
 */

@AndroidEntryPoint
class CouponActivity : BaseActivity<ActivityCouponBinding>(R.layout.activity_coupon) {

    private var disposable: Disposable? = null
    private var couponCode: String = ""

    private val couponBinding by lazy { ActivityCouponBinding.inflate(layoutInflater) }
    private val vm by viewModels<CouponViewModel>()

    //    private lateinit var vm: CouponViewModel
    var mContext: Context = this@CouponActivity
    var mUserInputCode: String? = null

    // 쿠폰 Dialog Title, subTitle 변수 - jhm 2022/10/27
    private var title: String = "";
    private var subTitle: String = "";


    @SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.let {
            LogUtil.logE("let")
        }
        binding.run {
            LogUtil.logE("run")
        }
        binding.apply {
            lifecycleOwner = this@CouponActivity

            couponBinding.lifecycleOwner = this@CouponActivity
            couponBinding.activity = this@CouponActivity
            couponVm = vm

            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#f2f3f4") // 네비게이션 배경색

        }

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {
                // 바깥영역 터치시 키보드 내리기 - jhm 2022/09/14
                binding.root.setOnClickListener {
                    hideKeyboard()
                }

                // 닫기 - jhm 2022/09/14
                binding.backImg.setOnClickListener {
                    finish()
                }

                // 사용시 유의사항 Text Onclick - jhm 2022/09/14
                binding.noticeText.setOnClickListener {
                    // 유의사항 text 정렬 - jhm 2022/09/14
                    DialogManager.openLeftDalog(
                        mContext, "사용시 유의사항",
                        mContext.getString(R.string.notice),
                        "확인했어요!"
                    )
                }


                val userCouponCodeObserver =
                    Observer { userInputCode: String ->
                        mUserInputCode = userInputCode
                    }

                vm.getCodeInput().observe(this@CouponActivity, userCouponCodeObserver)
                vm.getCodeInput().observe(this@CouponActivity, Observer { it ->
                    mUserInputCode = it
                    LogUtil.logE("입력값 : $mUserInputCode")
                    // 입력값이 빈값이면 clear Icon Gone - jhm 2022/09/13
                    if (mUserInputCode.toString().isEmpty()) {
                        binding.clearIcon.visibility = View.GONE
                        binding.confirmBtn.isSelected = false
                    }
                    // 입력값이 1글자라도 있으면 clear Icon Visible - jhm 2022/09/13
                    else {
                        binding.clearIcon.visibility = View.VISIBLE
                        binding.confirmBtn.isSelected = true

                        // input text clear - jhm 2022/09/13
                        binding.clearIcon.setOnClickListener {
                            binding.editText.setText("")
                        }

                        binding.confirmBtn.setOnClickListener {
                            val customDialogListener: CustomDialogListener =
                                object : CustomDialogListener {
                                    override fun onCancelButtonClicked() {
                                        LogUtil.logE("닫기 OnClick..")
                                    }

                                    override fun onOkButtonClicked() {
                                        LogUtil.logE("쿠폰 사용하기 OnClick 완료")
                                        vm.getCoupon(mUserInputCode.toString())
                                        vm.responseCode.observe(this@CouponActivity) {
                                            var response = it
                                            if (response.equals("200")) {
                                                title = "쿠폰 사용 완료"
                                                subTitle = "쿠폰이 사용 되었어요!"
                                            } else if (response.equals("208")) {
                                                title = "쿠폰 등록 실패"
                                                subTitle = "이미 사용된 쿠폰이에요."
                                            } else if (response.equals("400")) {
                                                title = "쿠폰 등록 실패"
                                                subTitle = "회원정보가 일치하지 않아요."
                                            } else if (response.equals("404")) {
                                                title = "쿠폰 등록 실패"
                                                subTitle = "쿠폰번호가 유효하지 않아요."
                                            } else {
                                                title = "쿠폰 등록 실패"
                                                subTitle = "사용 가능한 쿠폰이 아닙니다!"
                                            }
                                        }
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            DialogManager.openNotGoDalog(mContext, title, subTitle)
                                        }, 400)
                                    }
                                }
                            DialogManager.openTwoBtnDialog(
                                mContext,
                                "쿠폰을 바로 사용할까요?",
                                "등록 완료된 쿠포은 취소할 수 없어요.",
                                customDialogListener,
                                "쿠폰 사용"
                            )
                        }
                    }
                })
            } else {
                val intent = Intent(applicationContext, NetworkActivity::class.java)
                startActivity(intent)
            }
        }



    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        LogUtil.logE("CouponActivity onStart..")
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.let { disposable!!.dispose() }
    }


    // 키보드 숨기기 - jhm 2022/09/14
    private fun hideKeyboard() {
        if (this.currentFocus != null) {
            val inputManager: InputMethodManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                this.currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
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