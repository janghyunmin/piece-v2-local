package com.bsstandard.piece.view.deleteMember

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datamodel.dmodel.member.MemberWithdrawModel
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.MemberDeleteDTO
import com.bsstandard.piece.databinding.ActivityDeletememberDetailBinding
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.widget.utils.CommonTwoTypeDialog
import com.bsstandard.piece.widget.utils.CustomDialogListener
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response

/**
 *packageName    : com.bsstandard.piece.view.withdraw
 * fileName       : WithdrawDetailActivity
 * author         : piecejhm
 * date           : 2022/09/20
 * description    : 회원탈퇴 화면2 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/20        piecejhm       최초 생성
 */

@AndroidEntryPoint
class DeleteMemberDetailActivity :
    BaseActivity<ActivityDeletememberDetailBinding>(R.layout.activity_deletemember_detail) {
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    val mContext: Context = this@DeleteMemberDetailActivity
    var checked: Boolean = false // 체크박스 초기화 - jhm 2022/09/20
    val response: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)

    // 회원 탈퇴 요청시 Model 필요 값 - jhm 2022/09/20
    private var withdrawalReasonCode: String? = ""
    private var withdrawalReasonText: String? = ""

    // 탈퇴 불가시 필요한 Dialog - jhm 2022/09/20
    private var commonTwoTypeModal: CommonTwoTypeDialog? = null

    companion object {
        const val TAG: String = "DeleteMemberDetailActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        withdrawalReasonCode = intent.extras!!.getString("withdrawalReasonCode")
        withdrawalReasonText = intent.extras!!.getString("withdrawalReasonText")

        LogUtil.logE("withdrawalReasonCode : $withdrawalReasonCode")
        LogUtil.logE("withdrawalReasonText : $withdrawalReasonText")

        binding.apply {
            binding.lifecycleOwner = this@DeleteMemberDetailActivity
            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색


            // 예치금 출금 신청하기 버튼 - jhm 2022/09/20
            binding.withdrawLayout.setOnClickListener {
                LogUtil.logE("예치금 출금 신청하기 OnClick..")
            }

            // 소유조각 보러가기 버튼 - jhm 2022/09/20
            binding.ownLayout.setOnClickListener {
                LogUtil.logE("소유 조각 보러가기 OnClick..")
            }

            binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                checked = isChecked
                if (checked) {
                    LogUtil.logE("체크 완료 $checked")
                } else {
                    LogUtil.logE("체크 해제 완료 $checked")
                }
            }

            // 상단 화살표 Activity finish - jhm 2022/09/20
            binding.backImg.setOnClickListener {
                finish()
            }

            // 하단 취소 버튼 OnClick - jhm 2022/09/20
            binding.cancleBtn.setOnClickListener {
                finish()
            }

            // 탈퇴시 필요 모델 Set - jhm 2022/09/20
            val memberWithdrawModel =
                MemberWithdrawModel(withdrawalReasonCode, withdrawalReasonText)
            // 탈퇴 불가시 Dialog Setting - jhm 2022/09/20
            val customDialogListener: CustomDialogListener =
                object : CustomDialogListener {
                    @RequiresApi(Build.VERSION_CODES.R)
                    override fun onOkButtonClicked() {
                        LogUtil.logE("카카오톡 문의하기 OnClick..")
                    }

                    override fun onCancelButtonClicked() {
                        LogUtil.logE("취소 OnClick..")
                        commonTwoTypeModal?.dismiss()
                    }
                }

            // 탈퇴 성공시 Dialog Setting - jhm 2022/09/21
            val customDeleteDialogListener: CustomDialogListener =
                object : CustomDialogListener {
                    @RequiresApi(Build.VERSION_CODES.R)
                    override fun onOkButtonClicked() {
                        LogUtil.logE("체크 완료시 탈퇴 API Call..")
                        response?.deleteMember(
                            "Bearer $accessToken",
                            deviceId,
                            memberId,
                            memberWithdrawModel
                        )?.enqueue(object : retrofit2.Callback<MemberDeleteDTO> {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onResponse(
                                call: Call<MemberDeleteDTO>,
                                response: Response<MemberDeleteDTO>
                            ) {
                                LogUtil.logE(
                                    "member delete call success.." + response.body()?.toString()
                                )
                                commonTwoTypeModal?.dismiss()
                                try {
                                    if (!response.body().toString().isEmpty()) {
                                        if (response.isSuccessful) {
                                            when (response.code()) {
                                                // 탈퇴 성공 - jhm 2022/09/21
                                                200 -> {
                                                    LogUtil.logE(
                                                        "Code : ${response.code()} + \n " +
                                                                "Message : ${response.body()?.message}"
                                                    )
                                                    // 탈퇴 성공시 탈퇴 완료 Activity 이동 - jhm 2022/09/21
                                                    val intent = Intent(mContext, DeleteMemberSuccessActivity::class.java)
                                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                    startActivity(intent)
                                                    finishAffinity()

                                                }
                                                // 탈퇴 불가 - jhm 2022/09/21
                                                202 -> {
                                                    LogUtil.logE(
                                                        "Error Code : ${response.code()} + \n " +
                                                                "Error Message : ${response.body()?.message}"
                                                    )
                                                    commonTwoTypeModal = CommonTwoTypeDialog(
                                                        context = mContext,
                                                        "member_delete_n",
                                                        customDialogListener,
                                                        "",
                                                        "탈퇴 불가",
                                                        response.body()?.message
                                                    )
                                                    commonTwoTypeModal?.show()

                                                }
                                                // 서버 에러 - jhm 2022/09/21
                                                500 -> {
                                                    LogUtil.logE(
                                                        "Error Code : ${response.code()} + \n " +
                                                                "Error Message : ${response.body()?.message}"
                                                    )
                                                    // 서버 장애시 에러 처리 해야함 - jhm 2022/09/20

                                                }
                                            }
                                        } else {
                                            // 서버 장애시 에러 처리 해야함 - jhm 2022/09/20

                                            LogUtil.logE("서버 Error !" + response.message())
                                        }
                                    }
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                    LogUtil.logE("member delete Exception 발생 !")
                                }
                            }

                            override fun onFailure(call: Call<MemberDeleteDTO>, t: Throwable) {
                                LogUtil.logE("bookmark post call fail.." + t.stackTrace)

                            }
                        })

                    }

                    override fun onCancelButtonClicked() {
                        LogUtil.logE("취소 OnClick..")
                        commonTwoTypeModal?.dismiss()

                    }
                }

            // 하단 탈퇴 버튼 OnClick - jhm 2022/09/20
            binding.confirmBtn.setOnClickListener {
                if (checked) {
                    commonTwoTypeModal = CommonTwoTypeDialog(
                        context = mContext,
                        "member_delete_real",
                        customDeleteDialogListener,
                        "",
                        "정말로 탈퇴하시겠습니까?",
                        "회원 탈퇴시 90일간 재가입이 불가능해요."
                    )
                    commonTwoTypeModal?.show()
                }
                else {
                    LogUtil.logE("체크 완료해주세요..")
                }
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