package com.bsstandard.piece.view.purchase

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datamodel.dmodel.purchase.PurchaseModel
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.BaseDTO
import com.bsstandard.piece.databinding.ActivityPurchaseResultBinding
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 *packageName    : com.bsstandard.piece.view.purchase
 * fileName       : PurchaseResultActivity
 * author         : piecejhm
 * date           : 2022/10/21
 * description    : 포트폴리오 구매 결과 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/21        piecejhm       최초 생성
 */

@AndroidEntryPoint
class PurchaseResultActivity :
    BaseActivity<ActivityPurchaseResultBinding>(R.layout.activity_purchase_result) {
    val mContext: Context = this@PurchaseResultActivity
    val response: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")
    val memberAppVersion: String = PrefsHelper.read("appVersion", "")


    @SuppressLint("SetTextI18n", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // UI Setting 최종 - jhm 2022/09/14
        setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
        setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
        setNaviBarIconColor(true) // 네비게이션 true : 검정색
        setNaviBarBgColor("#ffffff") // 네비게이션 배경색

        binding.apply {
            lifecycleOwner = this@PurchaseResultActivity
            binding.activity = this@PurchaseResultActivity
        }


        val portfolioId = PrefsHelper.read("portfolioTitle", "")
        val purchaseVolume = PrefsHelper.read("purchaseVolume", "")

        LogUtil.logE("portfolioTitle : $portfolioId")
        LogUtil.logE("purchaseVolume : $purchaseVolume")

        // 포트폴리오 Post 구매시 필요 모델 - jhm 2022/10/25
        val purchaseModel = PurchaseModel(portfolioId, purchaseVolume.toInt())

        // 포트폴리오 구매 Post Api Call - jhm 2022/10/25
        try {
            response?.buyPurchase(
                "Bearer $accessToken",
                deviceId,
                memberAppVersion,
                memberId,
                purchaseModel
            )
                ?.enqueue(object : Callback<BaseDTO> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<BaseDTO>,
                        response: Response<BaseDTO>
                    ) {
                        try {

                            LogUtil.logE("response.isSuccessful : ${response.isSuccessful}" )
                            LogUtil.logE("response.code : ${response.code()}" )
                            LogUtil.logE("response.message: ${response.message()}" )
                            LogUtil.logE("response.body.status: ${response.body()?.status}" )
                            LogUtil.logE("response.body.message: ${response.body()?.message}" )

                            if(response.code() == 200) {
                                LogUtil.logE("포트폴리오 구매 성공")
                                Glide.with(mContext).load(R.raw.purchase_complete_lopping)
                                    .into(binding.resultGif)

                                binding.resultTitle.text =
                                    "$portfolioId \n $purchaseVolume 피스를 구매했어요"

                                binding.errorCode.visibility = View.GONE
                                binding.retryMessage.visibility = View.GONE

                                binding.confirmBtn.setOnClickListener {
                                    btnOnClick()
                                }

                            } else {
                                LogUtil.logE("포트폴리오 구매 실패..")

                                Glide.with(mContext).load(R.drawable.withdraw_fail)
                                    .into(binding.resultGif)

                                binding.resultTitle.text = "구매에 실패했어요"

                                binding.errorCode.visibility = View.VISIBLE
                                binding.retryMessage.visibility = View.VISIBLE

                                binding.errorCode.text = response.code().toString()


                                binding.confirmBtn.setOnClickListener {
                                    btnOnClick()
                                }
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            LogUtil.logE("포트폴리오 구매 실패")
                        }
                    }

                    override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                        t.printStackTrace()
                        LogUtil.logE("포트폴리오 구매 하기 API 실패  ${t.message}")
                    }
                })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


    }

    private fun btnOnClick() {
        // 내지갑으로 이동 - jhm 2022/10/25
        var navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_main_fragment) as NavHostFragment?
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        var navController = navHostFragment?.navController
        navController?.navigate(R.id.FragmentWallet)
        transaction.addToBackStack(null)

        finish()

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