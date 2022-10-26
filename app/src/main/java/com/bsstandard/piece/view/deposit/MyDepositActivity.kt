package com.bsstandard.piece.view.deposit

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.AccountViewModel
import com.bsstandard.piece.data.viewmodel.DepositBalanceViewModel
import com.bsstandard.piece.data.viewmodel.DepositHistoryViewModel
import com.bsstandard.piece.databinding.ActivityMydepositBinding
import com.bsstandard.piece.view.bank.BankSelectActivity
import com.bsstandard.piece.view.withdrawal.WithdrawalActivity
import com.bsstandard.piece.widget.utils.CommonTwoTypeDialog
import com.bsstandard.piece.widget.utils.CustomDialogListener
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

/**
 *packageName    : com.bsstandard.piece.view.deposit
 * fileName       : MyDepositActivity
 * author         : piecejhm
 * date           : 2022/09/28
 * description    : 내지갑 상세 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/28        piecejhm       최초 생성
 *
 */

@AndroidEntryPoint
class MyDepositActivity : BaseActivity<ActivityMydepositBinding>(R.layout.activity_mydeposit),
    Listener {

    private lateinit var dvm: DepositBalanceViewModel // 출금 가능 금액 ViewModel - jhm 2022/09/29
    private lateinit var dhvm: DepositHistoryViewModel // 회원 거래 내역 목록 조회 ViewModel  - jhm 2022/09/29
    private lateinit var mvm: AccountViewModel // 회원 계좌 여부 조회 - jhm 2022/09/30
    
    val mContext: Context = this@MyDepositActivity
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    // 등록된 계좌가 없을때 Dialog - jhm 2022/09/20
    @Volatile private var commonTwoTypeModal: CommonTwoTypeDialog? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        dvm = ViewModelProvider(this@MyDepositActivity)[DepositBalanceViewModel::class.java]
        dhvm = ViewModelProvider(this@MyDepositActivity)[DepositHistoryViewModel::class.java]
        mvm = ViewModelProvider(this@MyDepositActivity)[AccountViewModel::class.java]

        binding.depositVm = dvm
        binding.historyVm = dhvm
        binding.memberAccountVm = mvm

        binding.lifecycleOwner = this@MyDepositActivity


        // UI Setting 최종 - jhm 2022/09/14
        setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
        setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
        setNaviBarIconColor(true) // 네비게이션 true : 검정색
        setNaviBarBgColor("#ffffff") // 네비게이션 배경색

        // 출금 가능 금액 - jhm 2022/09/30
        dvm.getDepositBalance(accessToken, deviceId, memberId)
        val decimal = DecimalFormat("#,###")
        var depositText : String = ""

        dvm.depoResponse.observe(this@MyDepositActivity, Observer {
            //binding.deposit.text =  decimal.format(it.data.depositBalance.toString()) + " 원"
            depositText = decimal.format(it.data.depositBalance)
            binding.deposit.text = "$depositText 원"
        })


        // 회원 거래 내역 목록 조회 요청 - jhm 2022/09/29
        // changeReason : ""-전체 , "MDR02"-조각 거래 내역, "MDR01"-예치금 입출금 내역 - jhm 2022/09/29
        dhvm.getDepositHistory(accessToken, deviceId, memberId, "", 100)
        dhvm.viewInit(binding.depositHistoryRv)


        // 출금 신청하기 버튼 클릭시 등록된 계좌 Dialog Setting - jhm 2022/09/20
        val customDialogListener: CustomDialogListener =
            object : CustomDialogListener {
                @RequiresApi(Build.VERSION_CODES.R)
                override fun onOkButtonClicked() {
                    LogUtil.logE("계좌 등록하기 OnClick..")

                    val intent = Intent(mContext, BankSelectActivity::class.java)
                    overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                    );
                    startActivity(intent)

                }

                override fun onCancelButtonClicked() {
                    LogUtil.logE("뒤로 OnClick..")
                    commonTwoTypeModal?.dismiss()
                    commonTwoTypeModal?.cancel()
                }
            }


        // 출금 신청하기 - jhm 2022/09/30
        binding.withdrawBtn.setOnClickListener {
            LogUtil.logE("출금 신청하기 OnClick..")

            // 등록된 계좌가 있는지 판별 - jhm 2022/09/30
            mvm.getAccount(accessToken, deviceId, memberId)
            mvm.accountResponse.observe(this@MyDepositActivity , Observer {
                LogUtil.logE("회원 계좌 조회 : " + it.data)

                try {
                    // 등록된 계좌가 없다면 계좌 등록으로 이동 - jhm 2022/09/30
                    if(it.data == null) {
                        commonTwoTypeModal = CommonTwoTypeDialog(
                            context = mContext,
                            "account_chk",
                            customDialogListener,
                            "",
                            "등록된 계좌가 없어요",
                            "지금 계좌를 등록할까요?"
                        )
                        commonTwoTypeModal?.show()
                        PrefsHelper.write("bankChk","N")
                    } else {
                        LogUtil.logE("등록된 계좌가 있음. 내 계좌로 출금 처리 진행")
                        LogUtil.logE("계좌 정보 : " + it.data.bankCode + "\n" + it.data.accountNo + "\n" + it.data.bankName)

                        PrefsHelper.write("bankChk","Y")
                        PrefsHelper.write("bankCode",it.data.bankCode)
                        PrefsHelper.write("bankName",it.data.bankName)
                    }
                } catch(e: Exception){
                    LogUtil.logE("계좌 조회 Exception : ${e.printStackTrace()}")
                }
            })

            // 등록된 계좌가 있다면 - jhm 2022/10/03
            if(PrefsHelper.read("bankChk","N").equals("Y")) {
                LogUtil.logE("등록된 계좌 있음")
                val intent = Intent(mContext, WithdrawalActivity::class.java)
                overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                );
                startActivity(intent)

            }
        }


        // 예치금 충전하기  - jhm 2022/10/05
        binding.chargeBtn.setOnClickListener {
            LogUtil.logE("예치금 충전하기 OnClick..")
            val intent = Intent(mContext, DepositChargeActivity::class.java)
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
            startActivity(intent)

        }


        // 하단 Select Box - jhm 2022/09/30
        val depositDialog = DepositDialog(mContext, "", this)
        binding.selectorLayout.setOnClickListener {
            LogUtil.logE("Select Box OnClick..")
            depositDialog.show(supportFragmentManager, "")
        }



    }

    override fun changeText(str: String?) {
        LogUtil.logE("다이얼로그 닫힐때 str : $str")
        when (str) {
            "전체" -> {
                binding.selected.text = str
                dhvm.getDepositHistory(accessToken, deviceId, memberId, "",100)
            }
            "조각 거래 내역" -> {
                binding.selected.text = str
                dhvm.getDepositHistory(accessToken, deviceId, memberId, "MDR02",100)
            }
            "예치금 입출금 내역" -> {
                binding.selected.text = str
                dhvm.getDepositHistory(accessToken, deviceId, memberId, "MDR01",100)
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