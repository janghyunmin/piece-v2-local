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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.AccountViewModel
import com.bsstandard.piece.data.viewmodel.DepositBalanceViewModel
import com.bsstandard.piece.databinding.ActivityMyaccountWithdrawBinding
import com.bsstandard.piece.view.deposit.Listener
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

/**
 *packageName    : com.bsstandard.piece.view.withdrawal
 * fileName       : WithDrawableActivity
 * author         : piecejhm
 * date           : 2022/10/03
 * description    : 내 계좌로 출금하기 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/03        piecejhm       최초 생성
 */

@AndroidEntryPoint
class WithdrawalActivity :
    BaseActivity<ActivityMyaccountWithdrawBinding>(R.layout.activity_myaccount_withdraw) , Listener, Observer<String>{
    private lateinit var dvm: DepositBalanceViewModel // 출금 가능 금액 ViewModel - jhm 2022/09/29
    private lateinit var mavm: AccountViewModel // 회원 계좌 정보 조회 ViewModel - jhm 2022/10/04
    val mContext: Context = this@WithdrawalActivity
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    private lateinit var nvm: NumberLiveViewModel

    // last live amount - jhm 2022/10/06
    private val sb = StringBuilder()

    // 내 계좌로 출금하기 liveText - jhm 2022/10/21
    private var liveText: MutableLiveData<String> = MutableLiveData()
    var money: String = ""


    companion object {
        const val TAG: String = "WithdrawalActivity"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.apply {
            binding.lifecycleOwner = this@WithdrawalActivity
            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

        }


        dvm = ViewModelProvider(this@WithdrawalActivity)[DepositBalanceViewModel::class.java]
        mavm = ViewModelProvider(this@WithdrawalActivity)[AccountViewModel::class.java]
        nvm = ViewModelProvider(this@WithdrawalActivity)[NumberLiveViewModel::class.java]

        binding.depositVm = dvm
        binding.memberAccountVm = mavm
        binding.numberPad = nvm

        // 출금 가능 금액 - jhm 2022/09/30
        dvm.getDepositBalance(accessToken, deviceId, memberId)


        dvm.depoResponse.observe(this@WithdrawalActivity, Observer {
            //binding.deposit.text =  decimal.format(it.data.depositBalance.toString()) + " 원"
            val decimal = DecimalFormat("#,###")
            var depositText: String = ""
            depositText = decimal.format(it.data.depositBalance)
            binding.depositNumber.text = "$depositText 원"
        })


        // 계좌 정보 - jhm 2022/10/04
        mavm.getAccount(accessToken, deviceId, memberId)
        mavm.accountResponse.observe(this@WithdrawalActivity, Observer {
            try {
                LogUtil.logE("bankCode : ${it.data.bankCode}" )
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

                binding.accountNumber.text = it.data.bankName + " " + it.data.accountNo

                binding.number.text = ""
                binding.number.hint = "얼마를 보낼까요?"
                binding.confirmBtn.isSelected = false


            } catch (e: Exception) {
                LogUtil.logE("회원 계좌 정보 조회 Error ! ${e.printStackTrace()}")
            }
        })


        val decimal = DecimalFormat("#,###")
        var depositText: String = ""
        liveText.observe(this@WithdrawalActivity, Observer {
            if (it.isNotEmpty()) {

                depositText = decimal.format(it.toInt())
                binding.number.text = "$depositText 원"

            } else {

                sb.setLength(0) // string builder 초기화 - jhm 2022/10/21
                money = "" // 입력값 초기화 - jhm 2022/10/21
                binding.number.text = ""
                binding.number.hint = "얼마를 보낼까요?"
                binding.confirmBtn.isSelected = false
            }
        })


        /**
         * 1~9 키패드 OnClick
         * **/
        binding.code1.setOnClickListener {
            if (sb.toString().length < 10) {
                money = sb.append("1").toString()
                liveText.value = money
                binding.confirmBtn.isSelected = true
            }

        }
        binding.code2.setOnClickListener {
            if (sb.toString().length < 10) {
                money = sb.append("2").toString()
                liveText.value = money
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code3.setOnClickListener {
            if (sb.toString().length < 10) {
                money = sb.append("3").toString()
                liveText.value = money
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code4.setOnClickListener {
            if (sb.toString().length < 10) {
                money = sb.append("4").toString()
                liveText.value = money
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code5.setOnClickListener {
            if (sb.toString().length < 10) {
                money = sb.append("5").toString()
                liveText.value = money
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code6.setOnClickListener {
            if (sb.toString().length < 10) {
                money = sb.append("6").toString()
                liveText.value = money
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code7.setOnClickListener {
            if (sb.toString().length < 10) {
                money = sb.append("7").toString()
                liveText.value = money
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code8.setOnClickListener {
            if (sb.toString().length < 10) {
                money = sb.append("8").toString()
                liveText.value = money
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code9.setOnClickListener {
            if (sb.toString().length < 10) {
                money = sb.append("9").toString()
                liveText.value = money
                binding.confirmBtn.isSelected = true
            }
        }
        binding.code0.setOnClickListener {
            if (sb.toString().length < 10) {
                if(sb.substring(0,money.length).equals("0")) {
                    binding.confirmBtn.isSelected = false
                } else {
                    money = sb.append("0").toString()
                    liveText.value = money
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

        // 확인 버튼 - jhm 2022/10/04
        binding.confirmBtn.setOnClickListener {
            if (binding.confirmBtn.isSelected) {
                LogUtil.logE("출금 확인 버튼 OnClick..")
                val intent = Intent(mContext, WithdrawalNextActivity::class.java)
                intent.putExtra("withdrawRequestAmount", binding.number.text.toString())
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent)
                finish()
            }
        }
    }
    // 마지막 입력값 제거 - jhm 2022/10/21
    fun removeLastNchars(str: String, n: Int): String {
        LogUtil.logE("str length : " + str.length)
        return str.substring(0, str.length - n)
    }

    // 뒤에서 부터 1자리씩 지우기 로직 - jhm 2022/10/21
    @SuppressLint("SetTextI18n")
    private fun removeNumber() {
        if (money.isNotEmpty()) {
            if(money.substring(0,0) == "0") {
                LogUtil.logE("앞에 0먼저 오면 반응 x")
            } else {
                money = sb.substring(0, money.length - 1).toString()
                liveText.value = money
                LogUtil.logE("money $money")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onChanged(t: String?) {
        LogUtil.logE("t $t")
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

    override fun changeText(str: String?) {
        TODO("Not yet implemented")
    }


}