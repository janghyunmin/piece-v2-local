package com.bsstandard.piece.view.bank

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.databinding.ActivityAccountSelectBinding
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint

/**
 *packageName    : com.bsstandard.piece.view.bank
 * fileName       : BankRegisterActivity
 * author         : piecejhm
 * date           : 2022/10/04
 * description    : 계좌 등록하기 (은행 선택) Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/04        piecejhm       최초 생성
 */

@AndroidEntryPoint
class BankSelectActivity : BaseActivity<ActivityAccountSelectBinding>(R.layout.activity_account_select){
    val mContext: Context = this@BankSelectActivity
    
    // 선택한 은행코드 , 은행이름 - jhm 2022/10/05
    var bankCode: String = ""
    var bankName: String = ""
    

    companion object {
        const val TAG: String = "BankRegisterActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            binding.lifecycleOwner = this@BankSelectActivity
            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

        }


        // 은행 선택 후 출금계좌 등록으로 이동 - jhm 2022/10/05
        selectBank()

    }

    private fun selectBank() {
        binding.bankLayout1.setOnClickListener {
            LogUtil.logE("KB 국민은행 선택")
            bankCode = "004"
            bankName = "KB 국민은행"
            bankRegister()
        }

        binding.bankLayout2.setOnClickListener {
            LogUtil.logE("신한은행 선택")
            bankCode = "026"
            bankName = "신한은행"
            bankRegister()
        }

        binding.bankLayout3.setOnClickListener {
            LogUtil.logE("하나은행 선택")
            bankCode = "005"
            bankName = "하나은행"
            bankRegister()
        }

        binding.bankLayout4.setOnClickListener {
            LogUtil.logE("경남은행 선택")
            bankCode = "039"
            bankName = "경남은행"
            bankRegister()
        }

        binding.bankLayout5.setOnClickListener {
            LogUtil.logE("광주은행 선택")
            bankCode = "034"
            bankName = "광주은행"
            bankRegister()
        }

        binding.bankLayout6.setOnClickListener {
            LogUtil.logE("대구은행 선택")
            bankCode = "031"
            bankName = "대구은행"
            bankRegister()
        }

        binding.bankLayout7.setOnClickListener {
            LogUtil.logE("부산은행 선택")
            bankCode = "032"
            bankName = "부산은행"
            bankRegister()
        }

        binding.bankLayout8.setOnClickListener {
            LogUtil.logE("수협은행 선택")
            bankCode = "007"
            bankName = "수협은행"
            bankRegister()
        }

        binding.bankLayout9.setOnClickListener {
            LogUtil.logE("우리은행 선택")
            bankCode = "020"
            bankName = "우리은행"
            bankRegister()
        }

        binding.bankLayout10.setOnClickListener {
            LogUtil.logE("전북은행 선택")
            bankCode = "037"
            bankName = "전북은행"
            bankRegister()
        }

        binding.bankLayout11.setOnClickListener {
            LogUtil.logE("제주은행 선택")
            bankCode = "035"
            bankName = "제주은행"
            bankRegister()
        }

        binding.bankLayout12.setOnClickListener {
            LogUtil.logE("카카오뱅크 선택")
            bankCode = "090"
            bankName = "카카오뱅크"
            bankRegister()
        }

        binding.bankLayout13.setOnClickListener {
            LogUtil.logE("케이뱅크 선택")
            bankCode = "089"
            bankName = "케이뱅크"
            bankRegister()
        }

        binding.bankLayout14.setOnClickListener {
            LogUtil.logE("토스뱅크 선택")
            bankCode = "092"
            bankName = "토스뱅크"
            bankRegister()
        }

        binding.bankLayout15.setOnClickListener {
            LogUtil.logE("한국 씨티은행 선택")
            bankCode = "027"
            bankName = "한국씨티은행"
            bankRegister()
        }

        binding.bankLayout16.setOnClickListener {
            LogUtil.logE("IBK 기업은행 선택")
            bankCode = "003"
            bankName = "IBK기업은행"
            bankRegister()
        }

        binding.bankLayout17.setOnClickListener {
            LogUtil.logE("KDB 산업은행 선택")
            bankCode = "002"
            bankName = "KDB산업은행"
            bankRegister()
        }

        binding.bankLayout18.setOnClickListener {
            LogUtil.logE("NH 농협은행 선택")
            bankCode = "011"
            bankName = "NH농협은행"
            bankRegister()
        }

        binding.bankLayout19.setOnClickListener {
            LogUtil.logE("SC제일은행 선택")
            bankCode = "023"
            bankName = "SC제일은행"
            bankRegister()
        }

        binding.bankLayout20.setOnClickListener {
            LogUtil.logE("우체국 선택")
            bankCode = "071"
            bankName = "우체국"
            bankRegister()
        }

        binding.bankLayout21.setOnClickListener {
            LogUtil.logE("외환은행 선택")
            bankCode = ""
            bankName = "외환은행"
            bankRegister()
        }

        binding.bankLayout22.setOnClickListener {
            LogUtil.logE("신협은행 선택")
            bankCode = "047"
            bankName = "신협"
            bankRegister()
        }

        binding.bankLayout23.setOnClickListener {
            LogUtil.logE("산림조합중앙회 선택")
            bankCode = "064"
            bankName = "산림조합중앙회"
            bankRegister()
        }

        binding.bankLayout24.setOnClickListener {
            LogUtil.logE("새마을금고 선택")
            bankCode = "045"
            bankName = "새마을금고"
            bankRegister()
        }
    }

    // 계좌 등록/변경하기 Activity 로 이동 - jhm 2022/10/05
    private fun bankRegister() {
        val intent = Intent(mContext, BankRegisterActivity::class.java)
        intent.putExtra("accountBankCode",bankCode)
        intent.putExtra("bankName",bankName)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent)
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