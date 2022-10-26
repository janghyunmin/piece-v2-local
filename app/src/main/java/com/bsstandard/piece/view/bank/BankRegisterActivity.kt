package com.bsstandard.piece.view.bank

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datamodel.dmodel.account.MemberBankAccountModel
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.BaseDTO
import com.bsstandard.piece.data.viewmodel.AccountRegisterViewModel
import com.bsstandard.piece.databinding.ActivityRegisterBinding
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.widget.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *packageName    : com.bsstandard.piece.view.bank
 * fileName       : BankRegisterActivity
 * author         : piecejhm
 * date           : 2022/10/05
 * description    : 출금계좌 등록 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/05        piecejhm       최초 생성
 */

@AndroidEntryPoint
class BankRegisterActivity : BaseActivity<ActivityRegisterBinding>(R.layout.activity_register) {

    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    val mContext: Context = this@BankRegisterActivity

    // 계좌등록 API - jhm 2022/10/05
    val response: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    // 넘겨받은 은행 정보 - jhm 2022/10/05
    private var accountBankCode: String? = ""
    private var bankName: String? = ""

    // 예금주 이름 - jhm 2022/10/05
    private var userName: String = ""

    private val vm by viewModels<AccountRegisterViewModel>()
    var mUserInputAccount: String? = null

    var customDialog: CustomDialog? = null


    companion object {
        const val TAG: String = "BankRegisterActivity"
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        keyboardVisibilityUtils =
            KeyboardVisibilityUtils(window, onShowKeyboard = { keyboardHeight ->
                sv_root.run {
                    smoothScrollTo(scrollX, scrollY + keyboardHeight)
                }
            })


        val intent = intent
        accountBankCode = intent.extras!!.getString("accountBankCode")
        bankName = intent.extras!!.getString("bankName")
        userName = PrefsHelper.read("name", "")

        LogUtil.logE("accountBankCode $accountBankCode")
        LogUtil.logE("bankName $bankName")



        binding.apply {
            binding.lifecycleOwner = this@BankRegisterActivity
            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

            registerVm = vm

            val accountNoObserver = Observer {
                InputAccountNo: String ->
                mUserInputAccount = InputAccountNo

            }

            vm.getAccountNum().observe(this@BankRegisterActivity, accountNoObserver)
            vm.getAccountNum().observe(this@BankRegisterActivity, Observer {
                mUserInputAccount = it
                LogUtil.logE("입력한 계좌번호 : $mUserInputAccount")

                if (binding.accountNumEdit.text.toString().isEmpty()) {
                    LogUtil.logE("계좌번호 입력값 없음")
                    binding.confirmBtn.isSelected = false
                } else {
                    LogUtil.logE("계좌번호 입력값 있음")
                    binding.confirmBtn.isSelected = true

                    binding.confirmBtn.setOnClickListener {
                        LogUtil.logE("회원 출금 계좌 등록(변경) 요청 실행.. ")

                        val customDialogListener: CustomDialogListener =
                            object : CustomDialogListener {
                                @RequiresApi(Build.VERSION_CODES.R)
                                override fun onOkButtonClicked() {
                                    LogUtil.logE("계좌 확인 실패 다시시도하기 OnClick..")
                                    customDialog?.dismiss()
                                }

                                override fun onCancelButtonClicked() {
                                    LogUtil.logE("취소 OnClick..")
                                }
                            }

                        val customNoListener: CustomDialogPassCodeListener =
                            object : CustomDialogPassCodeListener {
                                @RequiresApi(Build.VERSION_CODES.R)
                                override fun onCancleButtonClicked() {
                                    TODO("Not yet implemented")
                                }

                                override fun onRetryPassCodeButtonClicked() {
                                    TODO("Not yet implemented")
                                }
                            }


                        response?.postMemberAccount(
                            "Bearer $accessToken",
                            deviceId,
                            memberId,
                            memberBankAccountModel = MemberBankAccountModel(accountBankCode,binding.accountNumEdit.text.toString())
                        )?.enqueue(object : Callback<BaseDTO> {
                            override fun onResponse(call: Call<BaseDTO>, response: Response<BaseDTO>) {
                                try{
                                    LogUtil.logE("계좌 등록(변경) 성공..")

                                    // 계좌 변경 완료 페이지로 이동 - jhm 2022/10/05
                                    val intent = Intent(mContext,BankRegisterSuccessActivity::class.java)
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    startActivity(intent)
                                    finish()

                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                            }

                            override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                                LogUtil.logE("계좌 등록(변경) 실패.." + t.printStackTrace())

                                customDialog = CustomDialog(mContext,"account_fail",customDialogListener,customNoListener)
                                customDialog!!.show()
                            }
                        })


                    }
                }
            })

        }

        binding.bankTitle.text = bankName
        binding.name.text = userName


        // 계좌번호 EditText 클릭시 부모 title 색상 변경 - jhm 2022/10/05
        binding.accountNumEdit.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.accountNumText.setTextColor(mContext.getColor(R.color.c_4a4d55))
            } else {
                binding.accountNumText.setTextColor(mContext.getColor(R.color.c_b8bcc8))
            }
        }



        try {
            var statusIcon: String = ""
            LogUtil.logE("여기 $accountBankCode")
            when (accountBankCode) {
                "001" -> {
                    LogUtil.logE("한국 은행")
                }
                "002" -> {
                    LogUtil.logE("KDB 산업은행")
                    statusIcon = getURLForResource(R.drawable.bank02)
                }
                "003" -> {
                    LogUtil.logE("기업은행")
                    statusIcon = getURLForResource(R.drawable.bank03)
                }
                "004" -> {
                    LogUtil.logE("국민은행")
                    statusIcon = getURLForResource(R.drawable.bank04)
                }
                "005" -> {
                    LogUtil.logE("KEB 하나은행")
                    statusIcon = getURLForResource(R.drawable.bank05)
                }
                "007" -> {
                    LogUtil.logE("수협은행")
                    statusIcon = getURLForResource(R.drawable.bank07)
                }
                "008" -> {
                    LogUtil.logE("수출입 은행")
                }
                "011" -> {
                    LogUtil.logE("NH농협은행")
                    statusIcon = getURLForResource(R.drawable.bank11)
                }
                "020" -> {
                    LogUtil.logE("우리은행")
                    statusIcon = getURLForResource(R.drawable.bank20)
                }
                "023" -> {
                    LogUtil.logE("SC제일은행")
                    statusIcon = getURLForResource(R.drawable.bank23)
                }
                "026" -> {
                    LogUtil.logE("신한은행")
                    statusIcon = getURLForResource(R.drawable.bank26)
                }
                "027" -> {
                    LogUtil.logE("한국씨티은행")
                    statusIcon = getURLForResource(R.drawable.bank27)
                }
                "031" -> {
                    LogUtil.logE("대구은행")
                    statusIcon = getURLForResource(R.drawable.bank31)
                }
                "032" -> {
                    LogUtil.logE("부산은행")
                    statusIcon = getURLForResource(R.drawable.bank32)
                }
                "034" -> {
                    LogUtil.logE("광주은행")
                    statusIcon = getURLForResource(R.drawable.bank34)
                }
                "035" -> {
                    LogUtil.logE("제주은행")
                    statusIcon = getURLForResource(R.drawable.bank35)
                }
                "037" -> {
                    LogUtil.logE("전북은행")
                    statusIcon = getURLForResource(R.drawable.bank37)
                }
                "039" -> {
                    LogUtil.logE("경남은행")
                    statusIcon = getURLForResource(R.drawable.bank39)
                }
                "045" -> {
                    LogUtil.logE("새마을 금고")
                    statusIcon = getURLForResource(R.drawable.bank45)
                }
                "047" -> {
                    LogUtil.logE("신협")
                    statusIcon = getURLForResource(R.drawable.bank47)
                }
                "064" -> {
                    LogUtil.logE("산림조합중앙회")
                    statusIcon = getURLForResource(R.drawable.bank64)
                }
                "071" -> {
                    LogUtil.logE("우체국")
                    statusIcon = getURLForResource(R.drawable.bank71)
                }
                "089" -> {
                    LogUtil.logE("케이뱅크")
                    statusIcon = getURLForResource(R.drawable.bank89)
                }
                "090" -> {
                    LogUtil.logE("카카오 뱅크")
                    statusIcon = getURLForResource(R.drawable.bank90)
                }
                "092" -> {
                    LogUtil.logE("토스뱅크")
                    statusIcon = getURLForResource(R.drawable.bank92)
                }
            }
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(30))
            Glide.with(mContext)
                .load(statusIcon)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.bankIcon)

        } catch (ex: Exception) {
            ex.printStackTrace()
            LogUtil.logE("은행 이미지 불러오기 실패..")
        }


        // 변경 버튼 OnClick - jhm 2022/10/05
        binding.selectBtn.setOnClickListener {
            LogUtil.logE("변경 버튼 OnClick..")
            finish()
        }

    }

    // 은행 코드에 따른 은행 아이콘 이미지 load - jhm 2022/10/04
    private fun getURLForResource(resId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.getPackage().getName() + "/" + resId)
            .toString()
    }

    override fun onDestroy() {
        keyboardVisibilityUtils.detachKeyboardListeners()
        super.onDestroy()
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