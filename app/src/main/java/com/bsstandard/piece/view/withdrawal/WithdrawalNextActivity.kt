package com.bsstandard.piece.view.withdrawal

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.bsstandard.piece.data.datamodel.dmodel.withdraw.WithdrawModel
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.BaseDTO
import com.bsstandard.piece.data.viewmodel.AccountViewModel
import com.bsstandard.piece.data.viewmodel.DepositBalanceViewModel
import com.bsstandard.piece.databinding.ActivityNextWithdrawBinding
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.NetworkConnection
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat


/**
 *packageName    : com.bsstandard.piece.view.withdrawal
 * fileName       : WithdrawalNextActivity
 * author         : piecejhm
 * date           : 2022/10/04
 * description    : 내계좌로 출금 상세 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/04        piecejhm       최초 생성
 */

@AndroidEntryPoint
class WithdrawalNextActivity :
    BaseActivity<ActivityNextWithdrawBinding>(R.layout.activity_next_withdraw) {
    private lateinit var dvm: DepositBalanceViewModel // 출금 가능 금액 ViewModel - jhm 2022/09/29
    private lateinit var mavm: AccountViewModel // 회원 계좌 정보 조회 ViewModel - jhm 2022/10/04
    val mContext: Context = this@WithdrawalNextActivity
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    val response: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)

    var returnText: Int = 0

    // 입력한 금액 - jhm 2022/10/04
    var withdrawRequestAmount: String = ""

    // 회원 출금계좌 명 - jhm 2022/10/04
    var bankName: String = ""

    // 출금 후 예치금 잔액 - jhm 2022/10/07


    companion object {
        const val TAG: String = "WithdrawalNextActivity"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            binding.lifecycleOwner = this@WithdrawalNextActivity
            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

        }
        dvm = ViewModelProvider(this@WithdrawalNextActivity)[DepositBalanceViewModel::class.java]
        mavm = ViewModelProvider(this@WithdrawalNextActivity)[AccountViewModel::class.java]

        binding.depositVm = dvm
        binding.memberAccountVm = mavm

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {
                val intent = intent
                withdrawRequestAmount = intent.getStringExtra("withdrawRequestAmount").toString()
                LogUtil.logE("넘겨받은 값 : $withdrawRequestAmount")

                // 출금하려는 금액 - jhm 2022/10/04
                binding.number.text = withdrawRequestAmount + "을 보낼게요"

                // 출금하려는 금액뺀 나머지 잔존가액 - jhm 2022/10/04
                // 출금 가능 금액 - jhm 2022/09/30
                dvm.getDepositBalance(accessToken, deviceId, memberId)
                val decimal = DecimalFormat("#,###")
                var depositText: String = ""

                // 콤마 제거된 입력값 - jhm 2022/10/07
                var replace = withdrawRequestAmount.replace("[^\\d]".toRegex(), "")

                dvm.depoResponse.observe(this@WithdrawalNextActivity, Observer {
                    returnText = it.data.depositBalance.minus(replace.toInt())
                    LogUtil.logE("뺀 금액 : $returnText")
                    depositText = decimal.format(returnText)
                    binding.depositNumber.text = "$depositText 원"
                })

                // 계좌 정보 - jhm 2022/10/04
                mavm.getAccount(accessToken, deviceId, memberId)
                mavm.accountResponse.observe(this@WithdrawalNextActivity, Observer {
                    try {
                        var statusIcon: String = ""
                        when (it.data.bankCode) {
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

                            // 외환은행은 KEB 하나은행으로 통합 - jhm 2022/10/07
//                    "021" -> {
//                        LogUtil.logE("외환은행")
//                        statusIcon = getURLForResource(R.drawable.bank21)
//                    }
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

                binding.confirmBtn.setOnClickListener {
                    LogUtil.logE("출금하기 Button OnClick..")

                    response?.postDepositWithdraw(
                        "Bearer $accessToken",
                        deviceId,
                        memberId,
                        withdrawModel = WithdrawModel(
                            withdrawRequestAmount.replace(
                                "[^\\d]".toRegex(),
                                ""
                            ).toInt()
                        )
                    )?.enqueue(object : Callback<BaseDTO> {
                        override fun onResponse(call: Call<BaseDTO>, response: Response<BaseDTO>) {
                            try {
                                LogUtil.logE("출금신청 성공!")
                                val intent = Intent(mContext, WithdrawSuccessActivity::class.java)
                                intent.putExtra(
                                    "withdrawRequestAmount",
                                    withdrawRequestAmount.replace("[^\\d]".toRegex(), "")
                                )
                                intent.putExtra("bankName", bankName)
                                overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                                );
                                startActivity(intent)
                                finish()

                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                LogUtil.logE("Error ! " + ex.printStackTrace())

                                val intent = Intent(mContext, WithdrawFailActivity::class.java)
                                overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                                );
                                startActivity(intent)
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                            t.printStackTrace()
                            val intent = Intent(mContext, WithdrawFailActivity::class.java)
                            overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            );
                            startActivity(intent)
                            finish()

                            LogUtil.logE("출금 신청 실패 !")
                        }
                    })
                }

            } else {
                val intent = Intent(applicationContext, NetworkActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // 은행 코드에 따른 은행 아이콘 이미지 load - jhm 2022/10/04
    private fun getURLForResource(resId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.getPackage().getName() + "/" + resId)
            .toString()
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