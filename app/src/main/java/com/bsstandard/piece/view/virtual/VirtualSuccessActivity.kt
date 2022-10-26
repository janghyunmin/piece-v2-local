package com.bsstandard.piece.view.virtual

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.databinding.ActivityVirtualSuccessBinding
import com.bsstandard.piece.widget.utils.CustomDialog
import com.bsstandard.piece.widget.utils.CustomDialogListener
import com.bsstandard.piece.widget.utils.CustomDialogPassCodeListener
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 *packageName    : com.bsstandard.piece.view.virtual
 * fileName       : VirtualSuccessActivity
 * author         : piecejhm
 * date           : 2022/10/06
 * description    : 충전 신청 완료 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/06        piecejhm       최초 생성
 */

@AndroidEntryPoint
class VirtualSuccessActivity :
    BaseActivity<ActivityVirtualSuccessBinding>(R.layout.activity_virtual_success) {
    val mContext: Context = this@VirtualSuccessActivity
    var customDialog: CustomDialog? = null


    companion object {
        const val TAG: String = "VirtualSuccessActivity"
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            binding.lifecycleOwner = this@VirtualSuccessActivity
            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색

        }

        Glide.with(mContext).load(R.raw.deposit_charged_lopping).into(binding.chargeGif)


        var intent = intent
        var chargeMoney = intent.getStringExtra("chargeMoney").toString()
        LogUtil.logE("넘겨받은 충전할 금액 값 : $chargeMoney")


        binding.chargeMoney.text = chargeMoney


        var bankIconPath = getURLForResource(R.drawable.bank89)
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(30))
        Glide.with(mContext)
            .load(bankIconPath)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.bankIcon)

        binding.userName.text = PrefsHelper.read("name", "") + "님의 가상계좌에요."
        binding.accountNumber.text = "케이뱅크 " + PrefsHelper.read("cellPhoneNo", "").toString()


        // 현재 시간 출력 - jhm 2022/10/06
        var currentTime = LocalDateTime.now()
        currentTime.plusDays(1)

        // 입금 완료시간 - jhm 2022/10/06
        var formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd / HH시 mm분")
        currentTime.format(formatter)
        LogUtil.logE("currentTime : ${currentTime.format(formatter)}")
        binding.time.text = currentTime.format(formatter)


        // 계좌번호 복사하기 - jhm 2022/10/06
        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip =
            ClipData.newPlainText("accountNo", PrefsHelper.read("cellPhoneNo", "").toString())

        binding.copyText.setOnClickListener {
            clipboard.setPrimaryClip(clip)
            LogUtil.logE("가상계좌 번호 복사 완료")
        }


        binding.confirmBtn.setOnClickListener {
            // 입금하였는지 Dialog show - jhm 2022/10/06

            val customDialogListener: CustomDialogListener =
                object : CustomDialogListener {
                    @RequiresApi(Build.VERSION_CODES.R)
                    override fun onOkButtonClicked() {
                        LogUtil.logE("입금 하셨나요? 네 알겠습니다 OnClick..")
                        customDialog?.dismiss()
                        finish()
                    }

                    override fun onCancelButtonClicked() {
                        LogUtil.logE("취소 OnClick..")
                    }
                }
            val customNoListener: CustomDialogPassCodeListener =
                object : CustomDialogPassCodeListener {
                    @RequiresApi(Build.VERSION_CODES.R)
                    override fun onCancleButtonClicked() {
                        TODO("Not yet implemented")
                    }

                    override fun onRetryPassCodeButtonClicked() {
                        TODO("Not yet implemented")
                    }
                }

            customDialog =
                CustomDialog(mContext, "charge_confirm", customDialogListener, customNoListener)
            customDialog!!.show()


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