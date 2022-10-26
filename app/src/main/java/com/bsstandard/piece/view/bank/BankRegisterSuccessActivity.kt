package com.bsstandard.piece.view.bank

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.AccountViewModel
import com.bsstandard.piece.databinding.ActivityAccountSuccessBinding
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

/**
 *packageName    : com.bsstandard.piece.view.bank
 * fileName       : BankRegisterSuccessActivity
 * author         : piecejhm
 * date           : 2022/10/05
 * description    : 출금계좌 등록 성공시 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/05        piecejhm       최초 생성
 */


@AndroidEntryPoint
class BankRegisterSuccessActivity : BaseActivity<ActivityAccountSuccessBinding>(R.layout.activity_account_success){
    private lateinit var mavm: AccountViewModel // 회원 계좌 정보 조회 ViewModel - jhm 2022/10/04
    private var disposable: Disposable? = null

    var mContext: Context = this@BankRegisterSuccessActivity
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    // 회원 출금계좌 명 - jhm 2022/10/04
    var bankName: String = ""


    companion object {
        const val TAG: String = "BankRegisterSuccessActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@BankRegisterSuccessActivity

            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색
        }


        mavm = ViewModelProvider(this@BankRegisterSuccessActivity)[AccountViewModel::class.java]
        binding.memberAccountVm = mavm

        Glide.with(mContext).load(R.raw.withdraw_complete_lopping).into(binding.withdrawSuccessLottie)

        // 계좌 정보 - jhm 2022/10/04
        mavm.getAccount(accessToken, deviceId, memberId)
        mavm.accountResponse.observe(this@BankRegisterSuccessActivity, Observer {
            try {
                var statusIcon: String = ""
                when(it.data.bankCode) {
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
//                    "012" -> {
//                        LogUtil.logE("지역 농축협")
//                    }
                    "020" -> {
                        LogUtil.logE("우리은행")
                        statusIcon = getURLForResource(R.drawable.bank20)
                    }
                    "021" -> {
                        LogUtil.logE("외환은행")
                        statusIcon = getURLForResource(R.drawable.bank21)
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

                bankName = it.data.bankName
                binding.accountNumber.text = it.data.bankName + " " + it.data.accountNo
            } catch (e: Exception) {
                LogUtil.logE("회원 계좌 정보 조회 Error ! ${e.printStackTrace()}")
            }
        })

        // 확인 클릭시 Activity 종료 - jhm 2022/10/05
        binding.confirmBtn.setOnClickListener {
            finish()
        }

    }


    // 은행 코드에 따른 은행 아이콘 이미지 load - jhm 2022/10/04
    private fun getURLForResource(resId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.getPackage().getName() + "/" + resId)
            .toString()
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