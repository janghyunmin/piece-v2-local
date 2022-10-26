package com.bsstandard.piece.view.deed

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.databinding.ActivityDeedBinding
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

/**
 *packageName    : com.bsstandard.piece.view.deed
 * fileName       : DeedActivity
 * author         : piecejhm
 * date           : 2022/10/13
 * description    : 소유증서 상세 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/13        piecejhm       최초 생성
 */

@AndroidEntryPoint
class DeedActivity : BaseActivity<ActivityDeedBinding>(R.layout.activity_deed) {
    private var disposable: Disposable? = null
    private val activityDeedBinding by lazy { ActivityDeedBinding.inflate(layoutInflater) }
    var mContext: Context = this@DeedActivity


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@DeedActivity

            activityDeedBinding.lifecycleOwner = this@DeedActivity
            activityDeedBinding.activity = this@DeedActivity

            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#f2f3f4") // 네비게이션 배경색

        }

        val name = PrefsHelper.read("name", "")
        val birthDay = PrefsHelper.read("birthDay", "")

        // 이름 , 생년월일 - jhm 2022/10/14
        binding.title2.text = String.format(getString(R.string.deed_title_2),name,birthDay)

        // 제 3조 구매대상 및 대금지급 Data Setting - jhm 2022/10/14
        var intent = intent

        try {
            var title = intent.getStringExtra("title").toString()
            var size = intent.getIntExtra("size",0)
            var purchaseAt = intent.getStringExtra("purchaseAt").toString()


            var str = StringBuilder()
            for(i in 0 until size) {
                // 구매 대상 - jhm 2022/10/14
                LogUtil.logE("string : " + intent.getStringExtra("subTitle$i").toString())
                str.append(intent.getStringExtra("subTitle$i").toString(),",")
            }
            LogUtil.logE("str $str")

            binding.title.text = "$title($str)"


            var purchasePieceVolume = intent.getStringExtra("purchasePieceVolume").toString()
            var purchaseTotalAmount = intent.getStringExtra("purchaseTotalAmount").toString()
            var pieceVolume = intent.getStringExtra("pieceVolume").toString()
            var recruitmentAmount = intent.getStringExtra("recruitmentAmount").toString()

            LogUtil.logE("넘겨받은 title : $title")
            LogUtil.logE("넘겨받은 size : $size")

            LogUtil.logE("넘겨받은 purchasePieceVolume : $purchasePieceVolume")
            LogUtil.logE("넘겨받은 purchaseTotalAmount : $purchaseTotalAmount")
            LogUtil.logE("넘겨받은 pieceVolume : $pieceVolume")
            LogUtil.logE("넘겨받은 recruitmentAmount : $recruitmentAmount")



            // 구매 수량 - jhm 2022/10/14
            binding.amount.text = "$purchasePieceVolume / $pieceVolume"

            // 구매 대금 - jhm 2022/10/14
            binding.account.text = "$purchaseTotalAmount / $recruitmentAmount"

            // 구매 확인 - jhm 2022/10/14
            binding.confirm.text = "$title 호에 대한 분할 소유권 $purchasePieceVolume 개 구매"



            // 하단 구매자 정보 - jhm 2022/10/14
            binding.owner.text = "갑 : " + PrefsHelper.read("name","")
            binding.address.text = "주소 : " + PrefsHelper.read("baseAddress","") + " " + PrefsHelper.read("detailAddress","")
            binding.buyDate.text = "구매계약일 : $purchaseAt"


        } catch (ex: Exception) {
            ex.printStackTrace()
        }




    }

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