package com.bsstandard.piece.view.coupon

import android.annotation.SuppressLint
import android.content.Context
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
import com.bsstandard.piece.widget.utils.CommonTwoTypeDialog
import com.bsstandard.piece.widget.utils.CustomDialogListener
import com.bsstandard.piece.widget.utils.LogUtil
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

    // 쿠폰 등록하기 클릭시 필요한 Dialog - jhm 2022/09/13
    private var commonTwoTypeModal: CommonTwoTypeDialog? = null

    // 쿠폰 사용 완료 후 Dialog - jhm 2022/09/14
    private var commonTwoTypeReturnModal: CommonTwoTypeDialog? = null

    // 사용시 유의사항 - jhm 2022/09/14
    private var commonTwoTypeNoticeModal: CommonTwoTypeDialog? = null

    var mUserInputCode: String? = null


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
                getCouponNotice()

            }



            val userCouponCodeObserver =
                Observer { userInputCode: String ->
                    mUserInputCode = userInputCode
                }

            vm.getCodeInput().observe(this@CouponActivity, userCouponCodeObserver)
            vm.getCodeInput().observe(this@CouponActivity, Observer {
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
                                    commonTwoTypeModal?.dismiss()
                                }

                                override fun onOkButtonClicked() {
                                    LogUtil.logE("쿠폰 사용하기 OnClick 완료")
                                    vm.getCoupon(mUserInputCode.toString())
                                    vm.couponResponse.observe(this@CouponActivity) {
                                        commonTwoTypeModal?.dismiss()
                                        LogUtil.logE("vm.response : " + vm.couponResponse.value)
                                    }
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        getReturnMessage()
                                    }, 400)
                                }
                            }
                        commonTwoTypeModal = CommonTwoTypeDialog(
                            context = mContext,
                            "coupon",
                            customDialogListener,
                            "",
                            "",
                            ""
                        )
                        commonTwoTypeModal?.show()
                    }
                }
            })
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

    private fun getReturnMessage() {
        val customDialogListener_2: CustomDialogListener =
            object : CustomDialogListener {
                override fun onOkButtonClicked() {
                    LogUtil.logE("쿠폰 사용 완료 OnClick..")
                    commonTwoTypeReturnModal?.dismiss()
                }

                override fun onCancelButtonClicked() {
                    LogUtil.logE("쿠폰 취소 OnClick..")
                }
            }
        commonTwoTypeReturnModal = CommonTwoTypeDialog(
            context = mContext,
            "coupon_response",
            customDialogListener_2,
            vm.couponResponse.value?.message.toString(),
            "",
            ""
        )
        commonTwoTypeReturnModal?.show()
    }


    private fun getCouponNotice() {
        val customDialogListener_3: CustomDialogListener =
            object : CustomDialogListener {
                override fun onOkButtonClicked() {
                    commonTwoTypeNoticeModal?.dismiss()
                }

                override fun onCancelButtonClicked() {
                    TODO("Not yet implemented")
                }
            }
        commonTwoTypeNoticeModal = CommonTwoTypeDialog(
            context = mContext,
        "coupon_notice",
            customDialogListener_3,
            mContext.getString(R.string.notice),
            "",
            ""
        )
        commonTwoTypeNoticeModal?.show()
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