package com.bsstandard.piece.view.deposit

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.databinding.ActivityDepositChargeBinding
import com.bsstandard.piece.view.virtual.VirtualActivity
import com.bsstandard.piece.view.withdrawal.NumberLiveViewModel
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

/**
 *packageName    : com.bsstandard.piece.view.deposit
 * fileName       : DepositChargeActivity
 * author         : piecejhm
 * date           : 2022/10/05
 * description    : 나의 예치금 충전하기 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/05        piecejhm       최초 생성
 */

@AndroidEntryPoint
class DepositChargeActivity : BaseActivity<ActivityDepositChargeBinding>(R.layout.activity_deposit_charge) , Observer<String> {
    val mContext: Context = this@DepositChargeActivity

    private var textAmount: MutableLiveData<String> = MutableLiveData()

    private lateinit var nvm: NumberLiveViewModel

    // last live amount - jhm 2022/10/06
    private val sb = StringBuilder()
    
    // 충전 입금금액 넘겨줄 파라미터 변수 - jhm 2022/10/07
    private var liveText: MutableLiveData<String> = MutableLiveData()
    var chargeMoney: String = ""


    companion object {
        const val TAG: String = "DepositChargeActivity"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            binding.lifecycleOwner = this@DepositChargeActivity
            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

        }

        nvm = ViewModelProvider(this@DepositChargeActivity)[NumberLiveViewModel::class.java]
        binding.numberPad = nvm

        nvm.textAmount.observe(this,this)




        binding.number.text = ""
        binding.number.hint = "얼마를 충전할까요?"
        binding.confirmBtn.isSelected = false




        val decimal = DecimalFormat("#,###")
        var depositText: String = ""
        liveText.observe(this@DepositChargeActivity, Observer {
            if (it.isNotEmpty()) {

                depositText = decimal.format(it.toInt())
                binding.number.text = "$depositText 원"

            } else {

                sb.setLength(0) // string builder 초기화 - jhm 2022/10/21
                chargeMoney = "" // 입력값 초기화 - jhm 2022/10/21
                binding.number.text = ""
                binding.number.hint = "얼마를 보낼까요?"
                binding.confirmBtn.isSelected = false
            }
        })

        // 확인 버튼 - jhm 2022/10/04
        binding.confirmBtn.setOnClickListener {
            if (binding.confirmBtn.isSelected) {
                LogUtil.logE("충전 확인 버튼 OnClick..")
                val intent = Intent(mContext, VirtualActivity::class.java)
                intent.putExtra("chargeMoney", binding.number.text.toString()) // 충전할 금액 - jhm 2022/10/06
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent)
                finish()
            }
        }


        /**
         * 1~9 키패드 OnClick
         * **/
        binding.code1.setOnClickListener {
            if (sb.toString().length < 10) {
                chargeMoney = sb.append("1").toString()
                liveText.value = chargeMoney
                binding.confirmBtn.isSelected = true
            }

        }
        binding.code2.setOnClickListener {
            if (sb.toString().length < 10) {
                chargeMoney = sb.append("2").toString()
                liveText.value = chargeMoney
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code3.setOnClickListener {
            if (sb.toString().length < 10) {
                chargeMoney = sb.append("3").toString()
                liveText.value = chargeMoney
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code4.setOnClickListener {
            if (sb.toString().length < 10) {
                chargeMoney = sb.append("4").toString()
                liveText.value = chargeMoney
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code5.setOnClickListener {
            if (sb.toString().length < 10) {
                chargeMoney = sb.append("5").toString()
                liveText.value = chargeMoney
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code6.setOnClickListener {
            if (sb.toString().length < 10) {
                chargeMoney = sb.append("6").toString()
                liveText.value = chargeMoney
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code7.setOnClickListener {
            if (sb.toString().length < 10) {
                chargeMoney = sb.append("7").toString()
                liveText.value = chargeMoney
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code8.setOnClickListener {
            if (sb.toString().length < 10) {
                chargeMoney = sb.append("8").toString()
                liveText.value = chargeMoney
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code9.setOnClickListener {
            if (sb.toString().length < 10) {
                chargeMoney = sb.append("9").toString()
                liveText.value = chargeMoney
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code0.setOnClickListener {
            if (sb.toString().length < 10) {
                if(sb.substring(0,chargeMoney.length).equals("0")) {
                    binding.confirmBtn.isSelected = false
                } else {
                    chargeMoney = sb.append("0").toString()
                    liveText.value = chargeMoney
                    binding.confirmBtn.isSelected = true
                }
            }
        }





        // 1자리씩 삭제 - jhm 2022/10/21
        binding.clear.setOnClickListener {
            removeNumber()
        }

        // 초기화 버튼 - jhm 2022/10/21
        binding.clearText.setOnClickListener {
            sb.setLength(0) // string builder 초기화 - jhm 2022/10/21
            binding.number.text = ""
            binding.number.hint = "얼마를 보낼까요?"
            binding.confirmBtn.isSelected = false
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onChanged(t: String?) {

        chargeMoney = binding.number.text.toString()
        LogUtil.logE("chargeMoney $chargeMoney")

//        depositText = decimal.format(chargeMoney)
        binding.number.text = chargeMoney

    }
    // 마지막 입력값 제거 - jhm 2022/10/21
    fun removeLastNchars(str: String, n: Int): String {
        LogUtil.logE("str length : " + str.length)
        return str.substring(0, str.length - n)
    }

    // 뒤에서 부터 1자리씩 지우기 로직 - jhm 2022/10/21
    @SuppressLint("SetTextI18n")
    private fun removeNumber() {
        if (chargeMoney.isNotEmpty()) {
            if(chargeMoney.substring(0,0) == "0") {
                LogUtil.logE("앞에 0먼저 오면 반응 x")
            } else {
                chargeMoney = sb.substring(0, chargeMoney.length - 1).toString()
                liveText.value = chargeMoney
                LogUtil.logE("chargeMoney $chargeMoney")
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