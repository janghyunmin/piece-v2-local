package com.bsstandard.piece.view.consent

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datamodel.dmodel.consent.ConsentList
import com.bsstandard.piece.data.datamodel.dmodel.consent.UpdateConsentList
import com.bsstandard.piece.data.datamodel.dmodel.member.MemberModifyModel
import com.bsstandard.piece.data.datamodel.dmodel.member.NotificationModel
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.ConsentDTO
import com.bsstandard.piece.data.viewmodel.ConsentViewModel
import com.bsstandard.piece.data.viewmodel.MemberPutViewModel
import com.bsstandard.piece.databinding.ActivityConsentMoreBinding
import com.bsstandard.piece.view.adapter.consent.ConsentAdapter
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.view.webview.ConsentDetailWebView
import com.bsstandard.piece.widget.utils.Division
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import java.util.*

/**
 *packageName    : com.bsstandard.piece.view.consent
 * fileName       : MoreConsentActivity
 * author         : piecejhm
 * date           : 2022/09/21
 * description    : 이용약관 및 마케팅 정보 수신 동의 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/21        piecejhm       최초 생성
 */

@AndroidEntryPoint
class MoreConsentActivity :
    BaseActivity<ActivityConsentMoreBinding>(R.layout.activity_consent_more) {
    private val consentViewModel by viewModels<ConsentViewModel>() // consentViewModel - jhm 2022/09/21
    private val context: Context = this@MoreConsentActivity
    private var disposable: Disposable? = null

    private var consentAdapter: ConsentAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var consentList: ArrayList<ConsentList?>? =
        ArrayList() // 약관 adapter 연결 List - jhm 2022/09/22
    private var consents = ArrayList<UpdateConsentList>() // 정보 변경 요청시 모델 List - jhm 2022/09/22
    private var marketingContent: String = ""

    private var memberModifyModel: MemberModifyModel? = null // 회원 정보 변경시 필요 모델 - jhm 2022/09/21
    private val memberPutViewModel by viewModels<MemberPutViewModel>() // 회원 정보 변경 - jhm 2022/09/21

    // 초기 알림 설정 상태값 변수 - jhm 2022/09/21
    private var assetNotification: String? = PrefsHelper.read("assetNotification", "N")
    private var portfolioNotification: String? = PrefsHelper.read("portfolioNotification", "N")
    private var marketingSms: String? = PrefsHelper.read("marketingSms", "N")
    private var marketingApp: String? = PrefsHelper.read("marketingApp", "N")
    private var marketingAgree: String? = PrefsHelper.read("CON0907", "N")


    companion object {
        const val TAG: String = "MoreConsentActivity"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            binding.lifecycleOwner = this@MoreConsentActivity

            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

            mvConsent = consentViewModel
            // 회원 정보 변경 ViewModel - jhm 2022/09/21
            mvPut = memberPutViewModel
        }

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {
                consentViewModel.getConsentData("SIGN").observe(this@MoreConsentActivity, Observer {
                    consentList?.clear()


                    for (index in it.data.indices) {
                        consentList!!.add(
                            ConsentList(
                                true,
                                it.data[index].consentCode,
                                it.data[index].consentGroup,
                                it.data[index].consentTitle,
                                it.data[index].consentContents,
                                it.data[index].isMandatory,
                                it.data[index].displayOrder,
                                it.data[index].createdAt,
                                "Y",
                                Division.MORE_CONSENT
                            )
                        )
                    }
                    binding.marketingAgree.text =
                        "[선택] " + consentList?.get(7)?.consentTitle.toString() // 마케팅 활용 및 광고 수신 동의 - jhm 2022/09/22
                    marketingContent =
                        consentList?.get(7)?.consentContents.toString() // 마케팅 활용 및 광고 수신 동의 - jhm 2022/09/22
                    consentList!!.removeAt(consentList!!.size - 1)

                    // 약관 목록 UI - jhm 2022/09/22
                    ConsentUI()
                    consents.clear()
                })

                binding.marketingAppSwitch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        PrefsHelper.write("CON0907", "Y")
                        marketingAgree = "Y"
                    } else {
                        PrefsHelper.write("CON0907", "N")
                        marketingAgree = "N"
                    }
                    isChecked()
                    putAgreement()
                }

                isChecked()
            } else {
                val intent = Intent(applicationContext, NetworkActivity::class.java)
                startActivity(intent)
            }
        }


    }


    private fun ConsentUI() {
        consentAdapter = ConsentAdapter(context, consentList)
        layoutManager = LinearLayoutManager(context)
        binding.consentRV.layoutManager = layoutManager
        binding.consentRV.setHasFixedSize(true)
        binding.consentRV.adapter = consentAdapter


        // 약관 디테일 onClick - jhm 2022/06/21
        consentAdapter!!.setDetailClick { position ->
            val intent = Intent(context, ConsentDetailWebView::class.java)
            intent.putExtra("consentTitle", consentList!![position]!!.consentTitle)
            intent.putExtra("consentContents", consentList!![position]!!.consentContents)
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
            context.startActivity(intent)
        }
    }

    private fun putAgreement() {

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
                consents.add(
                    UpdateConsentList(
                        PrefsHelper.read("memberId", ""),
                        "CON0907",
                        marketingAgree
                    )
                )
            }
        }


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
            consents
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

    // 마케팅 활용 및 광고 수신 동의 Check 여부 - jhm 2022/09/22
    private fun isChecked() {
        if (marketingAgree.equals("Y")) {
            LogUtil.logE("마케팅 활용 및 광고 수신 동의 true")
            binding.marketingAppSwitch.isChecked = true
        } else {
            LogUtil.logE("마케팅 활용 및 광고 수신 동의 false")
            binding.marketingAppSwitch.isChecked = false
        }
    }


    // 메모리 누수 방지를 위해 자원 해제 - jhm 2022/08/08
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