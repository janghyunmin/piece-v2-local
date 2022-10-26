package com.bsstandard.piece.view.deleteMember

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.core.content.res.ResourcesCompat
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.databinding.ActivityDeletememberBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *packageName    : com.bsstandard.piece.view.withdraw
 * fileName       : WithdrawActivity
 * author         : piecejhm
 * date           : 2022/09/19
 * description    : 회원탈퇴 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/19        piecejhm       최초 생성
 */

@AndroidEntryPoint
class DeleteMemberActivity : BaseActivity<ActivityDeletememberBinding>(R.layout.activity_deletemember) {

    val mContext: Context = this@DeleteMemberActivity
    companion object {
        const val TAG: String = "DeleteMemberActivity"
    }

    // 회원 탈퇴 요청시 Model 필요 값 - jhm 2022/09/20
    private var withdrawalReasonCode: String? = ""
    private var withdrawalReasonText: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding.apply {
            binding.lifecycleOwner = this@DeleteMemberActivity



            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

        }


        // 사용하지 않는 앱이에요 OnClick - jhm 2022/09/20
        binding.reasonLayout1.setOnClickListener {
            changeUI("reason1")
        }
        // 수익률 회수기간이 너무 길어요 - jhm 2022/09/20
        binding.reasonLayout2.setOnClickListener {
            changeUI("reason2")
        }
        // 앱에 오류가 많아요 - jhm 2022/09/20
        binding.reasonLayout3.setOnClickListener {
            changeUI("reason3")
        }
        // 앱을 어떻게 쓰는지 모르겠아요 - jhm 2022/09/20
        binding.reasonLayout4.setOnClickListener {
            changeUI("reason4")
        }
        // 비슷한 서비스가 더 좋아요 - jhm 2022/09/20
        binding.reasonLayout5.setOnClickListener {
            changeUI("reason5")
        }
        // 기타 - jhm 2022/09/20
        binding.reasonLayout6.setOnClickListener {
            changeUI("reason6")
        }

        // 확인 버튼 클릭시 - jhm 2022/09/20
        binding.confirmBtn.setOnClickListener {
            val intent = Intent(mContext, DeleteMemberDetailActivity::class.java)
            intent.putExtra("withdrawalReasonCode",withdrawalReasonCode)
            intent.putExtra("withdrawalReasonText",withdrawalReasonText)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent)
        }
    }


    private fun changeUI(status: String) {
        val pre_regular = ResourcesCompat.getFont(mContext, R.font.pretendard_regular)
        val pre_bold = ResourcesCompat.getFont(mContext, R.font.pretendard_bold)
        when (status) {
            "reason1" -> {
                binding.reasonTitle1.setTextColor(mContext.getColor(R.color.c_10cfc9)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle1.typeface = pre_bold // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon1.visibility = View.VISIBLE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle2.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle2.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon2.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle3.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle3.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon3.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle4.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle4.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon4.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle5.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle5.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon5.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle6.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle6.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon6.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20
                binding.editText.visibility = View.GONE // 기타 선택시 EditText - jhm 2022/09/20

                binding.confirmBtn.isSelected = true

                withdrawalReasonCode = "MWR0101"
                withdrawalReasonText = "사용하지 않는 앱이에요"

            }
            "reason2" -> {
                binding.reasonTitle1.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle1.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon1.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle2.setTextColor(mContext.getColor(R.color.c_10cfc9)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle2.typeface = pre_bold // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon2.visibility = View.VISIBLE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle3.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle3.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon3.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle4.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle4.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon4.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle5.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle5.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon5.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle6.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle6.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon6.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20
                binding.editText.visibility = View.GONE // 기타 선택시 EditText - jhm 2022/09/20

                binding.confirmBtn.isSelected = true

                withdrawalReasonCode = "MWR0102"
                withdrawalReasonText = "수익률 회수기간이 너무 길어요"
            }
            "reason3" -> {
                binding.reasonTitle1.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle1.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon1.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle2.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle2.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon2.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle3.setTextColor(mContext.getColor(R.color.c_10cfc9)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle3.typeface = pre_bold // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon3.visibility = View.VISIBLE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle4.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle4.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon4.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle5.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle5.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon5.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle6.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle6.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon6.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20
                binding.editText.visibility = View.GONE // 기타 선택시 EditText - jhm 2022/09/20

                binding.confirmBtn.isSelected = true

                withdrawalReasonCode = "MWR0103"
                withdrawalReasonText = "앱에 오류가 많아요"
            }
            "reason4" -> {
                binding.reasonTitle1.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle1.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon1.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle2.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle2.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon2.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle3.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle3.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon3.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle4.setTextColor(mContext.getColor(R.color.c_10cfc9)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle4.typeface = pre_bold // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon4.visibility = View.VISIBLE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle5.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle5.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon5.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle6.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle6.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon6.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20
                binding.editText.visibility = View.GONE // 기타 선택시 EditText - jhm 2022/09/20

                binding.confirmBtn.isSelected = true

                withdrawalReasonCode = "MWR0104"
                withdrawalReasonText = "앱을 어떻게 쓰는지 모르겠어요"
            }
            "reason5" -> {
                binding.reasonTitle1.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle1.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon1.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle2.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle2.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon2.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle3.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle3.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon3.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle4.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle4.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon4.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle5.setTextColor(mContext.getColor(R.color.c_10cfc9)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle5.typeface = pre_bold // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon5.visibility = View.VISIBLE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle6.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle6.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon6.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20
                binding.editText.visibility = View.GONE // 기타 선택시 EditText - jhm 2022/09/20

                binding.confirmBtn.isSelected = true

                withdrawalReasonCode = "MWR0105"
                withdrawalReasonText = "비슷한 서비스가 더 좋아요"
            }
            "reason6" -> {
                binding.reasonTitle1.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle1.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon1.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle2.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle2.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon2.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle3.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle3.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon3.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle4.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle4.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon4.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle5.setTextColor(mContext.getColor(R.color.c_8c919f)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle5.typeface = pre_regular // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon5.visibility = View.GONE // 체크 Icon 생성 - jhm 2022/09/20

                binding.reasonTitle6.setTextColor(mContext.getColor(R.color.c_10cfc9)) // 색상 변경 - jhm 2022/09/20
                binding.reasonTitle6.typeface = pre_bold // 폰트 변경 - jhm 2022/09/20
                binding.checkIcon6.visibility = View.VISIBLE // 체크 Icon 생성 - jhm 2022/09/20
                binding.editText.visibility = View.VISIBLE

                binding.confirmBtn.isSelected = true

                withdrawalReasonCode = "MWR0106"
                withdrawalReasonText = "기타"
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