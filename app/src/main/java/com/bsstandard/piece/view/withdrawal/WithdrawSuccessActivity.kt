package com.bsstandard.piece.view.withdrawal

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.AccountViewModel
import com.bsstandard.piece.data.viewmodel.DepositBalanceViewModel
import com.bsstandard.piece.databinding.ActivityWithdrawSuccessBinding
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.MoneyFormatToWon
import com.bsstandard.piece.widget.utils.NetworkConnection
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

/**
 *packageName    : com.bsstandard.piece.view.withdrawal
 * fileName       : WithdrawSuccessActivity
 * author         : piecejhm
 * date           : 2022/10/04
 * description    : 예치금 출금 성공 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/04        piecejhm       최초 생성
 */

@AndroidEntryPoint
class WithdrawSuccessActivity :
    BaseActivity<ActivityWithdrawSuccessBinding>(R.layout.activity_withdraw_success) {
    private lateinit var dvm: DepositBalanceViewModel // 출금 가능 금액 ViewModel - jhm 2022/09/29
    private lateinit var mavm: AccountViewModel // 회원 계좌 정보 조회 ViewModel - jhm 2022/10/04
    val mContext: Context = this@WithdrawSuccessActivity
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    companion object {
        const val TAG: String = "WithdrawSuccessActivity"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            binding.lifecycleOwner = this@WithdrawSuccessActivity
            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

        }

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {
                Glide.with(mContext).load(R.raw.withdraw_complete_lopping)
                    .into(binding.withdrawSuccessLottie)


                val intent = intent
                var withdrawRequestAmount = intent.getStringExtra("withdrawRequestAmount")
                var bankName = intent.getStringExtra("bankName")
                LogUtil.logE("최종 넘겨받은 값 : $withdrawRequestAmount")


                binding.subTitle.text =
                    "예치금 ${MoneyFormatToWon.moneyFormatToWon(withdrawRequestAmount!!.toInt())} 원이 \n $bankName 계좌로 입금되었어요."


                // 확인 클릭시 나의 예치금 - jhm 2022/10/04
                binding.confirmBtn.setOnClickListener {
                    finish()
                }
            } else {
                val intent = Intent(applicationContext, NetworkActivity::class.java)
                startActivity(intent)
            }
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