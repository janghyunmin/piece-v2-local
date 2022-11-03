package com.bsstandard.piece.view.notisetting

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datamodel.dmodel.consent.UpdateConsentList
import com.bsstandard.piece.data.datamodel.dmodel.member.MemberModifyModel
import com.bsstandard.piece.data.datamodel.dmodel.member.NotificationModel
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.ConsentDTO
import com.bsstandard.piece.data.viewmodel.ConsentViewModel
import com.bsstandard.piece.data.viewmodel.GetUserViewModel
import com.bsstandard.piece.data.viewmodel.MemberPutViewModel
import com.bsstandard.piece.databinding.ActivityNotisettingBinding
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 *packageName    : com.bsstandard.piece.view.notisetting
 * fileName       : NotificationSettingActivity
 * author         : piecejhm
 * date           : 2022/09/21
 * description    : 더보기 - 기기 알림 설정 ( 회원 정보 변경 ViewModel )
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/21        piecejhm       최초 생성
 */

@AndroidEntryPoint
class NotificationSettingActivity :
    BaseActivity<ActivityNotisettingBinding>(R.layout.activity_notisetting) {
    val mContext: Context = this@NotificationSettingActivity
    private val mv by viewModels<GetUserViewModel>() // 회원 정보 조회 - jhm 2022/09/21

    private var memberModifyModel: MemberModifyModel? = null // 회원 정보 변경시 필요 모델 - jhm 2022/09/21
    private val memberPutViewModel by viewModels<MemberPutViewModel>() // 회원 정보 변경 - jhm 2022/09/21
    private val consentViewModel by viewModels<ConsentViewModel>() // 회원 정보 변경시 필요 consentViewModel - jhm 2022/09/21
    private var consentList = ArrayList<UpdateConsentList>()

    // 초기 알림 설정 상태값 변수 - jhm 2022/09/21
    private var assetNotification: String? = PrefsHelper.read("assetNotification", "N")
    private var portfolioNotification: String? = PrefsHelper.read("portfolioNotification", "N")
    private var marketingSms: String? = PrefsHelper.read("marketingSms", "N")
    private var marketingApp: String? = PrefsHelper.read("marketingApp", "N")

    companion object {
        const val TAG: String = "NotificationSettingActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            binding.lifecycleOwner = this@NotificationSettingActivity

            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

            // 내정보 조회 ViewModel API - jhm 2022/09/21
            memberVm = mv
            mv.getUserData()

            // 회원 정보 변경 ViewModel - jhm 2022/09/21
            mvPut = memberPutViewModel

            // 약관 리스트 ViewModel - jhm 2022/09/21
            mvConsent = consentViewModel
            consentList.clear()

        }


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {

                LogUtil.logE("assetNotification : $assetNotification")
                LogUtil.logE("portfolioNotification : $portfolioNotification")
                LogUtil.logE("marketingSms : $marketingSms")
                LogUtil.logE("marketingApp : $marketingApp")


                isChecked()

                // 자산 변동 switch - jhm 2022/09/21
                binding.assetNotificationSwitch.setOnCheckedChangeListener { _, isChecked ->
                    LogUtil.logE("assetNotificationSwitch : $isChecked")
                    if (isChecked) {
                        PrefsHelper.write("assetNotification", "Y")
                        assetNotification = "Y"
                    } else {
                        PrefsHelper.write("assetNotification", "N")
                        assetNotification = "N"
                    }
                    isChecked()
                    getModelData()
                }
                // 포트폴리오 switch - jhm 2022/09/21
                binding.portfolioNotificationSwitch.setOnCheckedChangeListener { _, isChecked ->
                    LogUtil.logE("portfolioNotificationSwitch : $isChecked")
                    if (isChecked) {
                        PrefsHelper.write("portfolioNotification", "Y")
                        portfolioNotification = "Y"
                    } else {
                        PrefsHelper.write("portfolioNotification", "N")
                        portfolioNotification = "N"
                    }
                    isChecked()
                    getModelData()
                }
                // 문자 알림 switch - jhm 2022/09/21
                binding.marketingSmsSwitch.setOnCheckedChangeListener { _, isChecked ->
                    LogUtil.logE("marketingSmsSwitch : $isChecked")
                    if (isChecked) {
                        PrefsHelper.write("marketingSms", "Y")
                        marketingSms = "Y"
                    } else {
                        PrefsHelper.write("marketingSms", "N")
                        marketingSms = "N"
                    }
                    isChecked()
                    getModelData()
                }
                // 앱 알림 switch - jhm 2022/09/21
                binding.marketingAppSwitch.setOnCheckedChangeListener { _, isChecked ->
                    LogUtil.logE("marketingAppSwitch : $isChecked")
                    if (isChecked) {
                        PrefsHelper.write("marketingApp", "Y")
                        marketingApp = "Y"
                    } else {
                        PrefsHelper.write("marketingApp", "N")
                        marketingApp = "N"
                    }
                    isChecked()
                    getModelData()
                }
            } else {
                val intent = Intent(applicationContext, NetworkActivity::class.java)
                startActivity(intent)
            }
        }



    }


    // 알림 설정 Y/N - jhm 2022/09/21
    private fun isChecked() {
        if (assetNotification.equals("Y")) {
            LogUtil.logE("자산변동 true")
            binding.assetNotificationSwitch.isChecked = true
        } else {
            LogUtil.logE("자산변동 false")
            binding.assetNotificationSwitch.isChecked = false
        }

        // 포트폴리오 switch - jhm 2022/09/21
        if (portfolioNotification.equals("Y")) {
            LogUtil.logE("포트폴리오 변동 true")
            binding.portfolioNotificationSwitch.isChecked = true
        } else {
            LogUtil.logE("포트폴리오 변동 false")
            binding.portfolioNotificationSwitch.isChecked = false
        }

        // 문자 알림 switch - jhm 2022/09/21
        if (marketingSms.equals("Y")) {
            LogUtil.logE("문자 알림 true")
            binding.marketingSmsSwitch.isChecked = true
        } else {
            LogUtil.logE("문자 알림 false")
            binding.marketingSmsSwitch.isChecked = false
        }

        // 앱 알림 switch - jhm 2022/09/21
        if (marketingApp.equals("Y")) {
            LogUtil.logE("앱 알림 true")
            binding.marketingAppSwitch.isChecked = true
        } else {
            LogUtil.logE("앱 알림 false")
            binding.marketingAppSwitch.isChecked = false
        }

    }

    private fun getModelData() {
        val memberId = PrefsHelper.read("memberId", "")
        val name = PrefsHelper.read("name", "")
        val pinNumber = ""
        val cellPhoneNo = PrefsHelper.read("cellPhoneNo", "")
        val cellPhoneIdNo = PrefsHelper.read("cellPhoneIdNo", "")
        val birthDay = PrefsHelper.read("birthDay", "")
        val zipCode = ""
        val baseAddress: String = ""
        val detailAddress: String = ""
        val ci = PrefsHelper.read("ci", "")
        val di = PrefsHelper.read("di", "")
        val gender = PrefsHelper.read("gender", "")
        val email = PrefsHelper.read("email", "")
        val isFido = PrefsHelper.read("isFido", "")

        consentViewModel.getConsentData("SIGN").observe(
            this
        ) { response: ConsentDTO ->
            for (index in response.data.indices) {
                consentList.add(
                    UpdateConsentList(
                        PrefsHelper.read("memberId", ""),
                        response.data[index].consentCode,
                        "Y"
                    )
                )
            }
        }

        LogUtil.logE("변경됨? : $assetNotification")

        val notification = NotificationModel(
            PrefsHelper.read("memberId", ""),
            assetNotification,
            portfolioNotification,
            marketingSms,
            marketingApp
        )
        memberModifyModel = MemberModifyModel(
            memberId,
            name,
            pinNumber,
            cellPhoneNo,
            cellPhoneIdNo,
            birthDay,
            zipCode,
            baseAddress,
            detailAddress,
            ci,
            di,
            gender,
            email,
            isFido,
            notification,
            consentList
        )

        memberPutViewModel.putCallMemberData(memberModifyModel)
        memberPutViewModel.memberPutData.observe(
            Objects.requireNonNull(this),
            Observer { memberPutDTO ->
                LogUtil.logE("회원 정보 변경 완료 " + memberPutDTO.status)
                LogUtil.logE("assetNotification : " + memberPutDTO.data.notification.assetNotification)
                LogUtil.logE("portfolioNotification : " + memberPutDTO.data.notification.portfolioNotification)
                LogUtil.logE("marketingSms : " + memberPutDTO.data.notification.marketingSms)
                LogUtil.logE("marketingApp : " + memberPutDTO.data.notification.marketingApp)

            })
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