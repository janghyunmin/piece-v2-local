package com.bsstandard.piece.view.deed

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datamodel.dmodel.document.MemberPortfolioDocument
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.BaseDTO
import com.bsstandard.piece.data.viewmodel.GetUserViewModel
import com.bsstandard.piece.databinding.ActivityDeedBinding
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.view.myInfo.MyInfoActivity
import com.bsstandard.piece.widget.utils.CustomDialogListener
import com.bsstandard.piece.widget.utils.DialogManager
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    private val mv by viewModels<GetUserViewModel>()
    val response: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")
    var purchaseId: String = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.apply {
            lifecycleOwner = this@DeedActivity

            activityDeedBinding.lifecycleOwner = this@DeedActivity
            activityDeedBinding.activity = this@DeedActivity

            memberVm = mv
            mv.getUserData()

            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#f2f3f4") // 네비게이션 배경색

        }


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {
                val name = PrefsHelper.read("name", "")
                val birthDay = PrefsHelper.read("birthDay", "")

                // 이름 , 생년월일 - jhm 2022/10/14
                binding.title2.text =
                    String.format(getString(R.string.deed_title_2), name, birthDay)

                // 제 3조 구매대상 및 대금지급 Data Setting - jhm 2022/10/14
                var intent = intent

                try {
                    var title = intent.getStringExtra("title").toString()
                    var size = intent.getIntExtra("size", 0)
                    var purchaseAt = intent.getStringExtra("purchaseAt").toString()


                    var str = StringBuilder()
                    for (i in 0 until size) {
                        // 구매 대상 - jhm 2022/10/14
                        LogUtil.logE("string : " + intent.getStringExtra("subTitle$i").toString())
                        str.append(intent.getStringExtra("subTitle$i").toString(), ",")
                    }
                    LogUtil.logE("str $str")

                    binding.title.text = "$title($str)"


                    var purchasePieceVolume =
                        intent.getStringExtra("purchasePieceVolume").toString()
                    var purchaseTotalAmount =
                        intent.getStringExtra("purchaseTotalAmount").toString()
                    var pieceVolume = intent.getStringExtra("pieceVolume").toString()
                    var recruitmentAmount = intent.getStringExtra("recruitmentAmount").toString()

                    LogUtil.logE("넘겨받은 title : $title")
                    LogUtil.logE("넘겨받은 size : $size")

                    LogUtil.logE("넘겨받은 purchasePieceVolume : $purchasePieceVolume")
                    LogUtil.logE("넘겨받은 purchaseTotalAmount : $purchaseTotalAmount")
                    LogUtil.logE("넘겨받은 pieceVolume : $pieceVolume")
                    LogUtil.logE("넘겨받은 recruitmentAmount : $recruitmentAmount")

                    purchaseId = intent.getStringExtra("purchaseId").toString()

                    LogUtil.logE("넘겨받은 purchaseId : $purchaseId")


                    // 구매 수량 - jhm 2022/10/14
                    binding.amount.text = "$purchasePieceVolume / $pieceVolume"

                    // 구매 대금 - jhm 2022/10/14
                    binding.account.text = "$purchaseTotalAmount / $recruitmentAmount"

                    // 구매 확인 - jhm 2022/10/14
                    binding.confirm.text = "$title 호에 대한 분할 소유권 $purchasePieceVolume 개 구매"


                    // 하단 구매자 정보 - jhm 2022/10/14
                    binding.owner.text = "갑 : " + PrefsHelper.read("name", "")
                    binding.address.text = "주소 : " + PrefsHelper.read(
                        "baseAddress",
                        ""
                    ) + " " + PrefsHelper.read("detailAddress", "")
                    binding.buyDate.text = "구매계약일 : $purchaseAt"


                } catch (ex: Exception) {
                    ex.printStackTrace()
                }


                // 주소 등록 Listener - jhm 2022/10/31
                val failAddressListener: CustomDialogListener = object : CustomDialogListener {
                    override fun onCancelButtonClicked() {
                        // 닫기 버튼 누른 후 로직 - jhm 2022/07/04
                        LogUtil.logE("cancel btn")
                    }

                    override fun onOkButtonClicked() {
                        // 주소 등록하러 가기 OnClick.. - jhm 2022/10/31
                        val intent = Intent(mContext, MyInfoActivity::class.java)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        startActivity(intent)
                    }
                }
                // 소유증서 우편으로 받기 Listener - jhm 2022/10/31
                val postAddressListener: CustomDialogListener = object : CustomDialogListener {
                    override fun onCancelButtonClicked() {
                        // 닫기 버튼 누른 후 로직 - jhm 2022/07/04
                        LogUtil.logE("cancel btn")
                    }

                    override fun onOkButtonClicked() {
                        // 소유증서 발송시 필요 모델 - jhm 2022/10/25
                        val memberPortfolioDocument =
                            MemberPortfolioDocument(memberId, purchaseId, "POST")
                        // 우편발송 API Call.. - jhm 2022/10/31
                        LogUtil.logE("우편 발송 API Call..")
                        try {
                            response?.postDocument(
                                accessToken = "Bearer $accessToken",
                                deviceId = deviceId,
                                memberId = memberId,
                                memberPortfolioDocument
                            )?.enqueue(object : Callback<BaseDTO> {
                                override fun onResponse(
                                    call: Call<BaseDTO>,
                                    response: Response<BaseDTO>
                                ) {
                                    try {
                                        LogUtil.logE("message : ${response.message()}")

                                        if (response.isSuccessful) {
                                            LogUtil.logE("소유증서 우편발송 성공 !!")
                                            DialogManager.openNotGoDalog(
                                                mContext,
                                                "우편 신청 완료",
                                                "소유 증서를 우편으로 보내드릴게요.\n받으실 때까지 최대 1주일이 걸릴 수 있어요."
                                            )
                                        } else {
                                            LogUtil.logE("소유증서 우편발송 실패 !!")
                                            DialogManager.openNotGoDalog(
                                                mContext,
                                                "우편 신청 실패",
                                                response.message()
                                            )
                                        }
                                    } catch (ex: Exception) {
                                        ex.printStackTrace()
                                        DialogManager.openNotGoDalog(
                                            mContext,
                                            "우편 신청 실패",
                                            response.message()
                                        )
                                        LogUtil.logE("우편발송 API Error.. : ${ex.message}")
                                    }
                                }

                                override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                                    t.printStackTrace()
                                    DialogManager.openNotGoDalog(
                                        mContext,
                                        "우편 신청 실패",
                                        t.message.toString()
                                    )
                                    LogUtil.logE("소유증서 우편 발송 Fail.. ${t.message}")
                                }
                            })
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            DialogManager.openNotGoDalog(
                                mContext,
                                "우편 신청 실패",
                                ex.message.toString()
                            )
                            LogUtil.logE("소유증서 우편 발송시 알수없는 Error ! ${ex.message}")
                        }
                    }
                }


                // 우편으로 받기 소유증서 신청 - jhm 2022/10/31
                binding.postBtn.setOnClickListener {
                    LogUtil.logE("우편으로 받기 OnClick..")
                    if (PrefsHelper.read("baseAddress", "")
                            .isEmpty() || PrefsHelper.read("detailAddress", "").isEmpty()
                    ) {
                        DialogManager.openTwoBtnDialog(
                            mContext,
                            "등록된 주소가 없어요",
                            "소유 증서를 받을 주소를 등록해주세요.",
                            failAddressListener,
                            "주소 등록"
                        )
                    } else {
                        DialogManager.openTwoBtnDialog(
                            mContext,
                            "소유 증서 우편으로 받기",
                            "등록된 주소로 소유 증서를 보내드려요.",
                            postAddressListener,
                            "우편"
                        )
                    }
                }


                // 등록된 이메일이 없어요 Listener - jhm 2022/10/31
                val failDialogEmailListener: CustomDialogListener = object : CustomDialogListener {
                    override fun onCancelButtonClicked() {
                        // 닫기 버튼 누른 후 로직 - jhm 2022/07/04
                        LogUtil.logE("cancel btn")
                    }

                    override fun onOkButtonClicked() {
                        // 이메일 등록하러 가기 OnClick..
                        LogUtil.logE("이메일 등록하러 가기 OnClick..")
                        val intent = Intent(mContext, MyInfoActivity::class.java)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        startActivity(intent)
                    }
                }

                // 이메일로 소유증서 신청 Listener - jhm 2022/10/31
                val postDialogEmailListener: CustomDialogListener = object : CustomDialogListener {
                    override fun onCancelButtonClicked() {
                        // 닫기 버튼 누른 후 로직 - jhm 2022/07/04
                        LogUtil.logE("cancel btn")
                    }

                    override fun onOkButtonClicked() {
                        // 이메일로 소유증서 신청 API Call..
                        LogUtil.logE("이메일 등록하러 가기 OnClick..")
                        // 소유증서 발송시 필요 모델 - jhm 2022/10/25
                        val memberPortfolioDocument =
                            MemberPortfolioDocument(memberId, purchaseId, "EMAIL")
                        // 이메일로 소유증서 발송 API Call.. - jhm 2022/10/31
                        LogUtil.logE("이메일로 발송 API Call..")
                        try {
                            response?.postDocument(
                                accessToken = "Bearer $accessToken",
                                deviceId = deviceId,
                                memberId = memberId,
                                memberPortfolioDocument
                            )?.enqueue(object : Callback<BaseDTO> {
                                override fun onResponse(
                                    call: Call<BaseDTO>,
                                    response: Response<BaseDTO>
                                ) {
                                    try {
                                        LogUtil.logE("message : ${response.message()}")

                                        if (response.isSuccessful) {
                                            LogUtil.logE("소유증서 이메일 발송 성공 !!")
                                            DialogManager.openNotGoDalog(
                                                mContext,
                                                "이메일 신청 완료",
                                                "소유증서를 이메일로 보내드릴게요 \n최대 1~5분의 시간이 소요될 수 있어요"
                                            )
                                        } else {
                                            LogUtil.logE("소유증서 이메일 발송 실패 !!")
                                            DialogManager.openNotGoDalog(
                                                mContext,
                                                "이메일 신청 실패",
                                                response.message()
                                            )
                                        }
                                    } catch (ex: Exception) {
                                        ex.printStackTrace()
                                        DialogManager.openNotGoDalog(
                                            mContext,
                                            "이메일 신청 실패",
                                            response.message()
                                        )
                                        LogUtil.logE("이메일 발송 API Error.. : ${ex.message}")
                                    }
                                }

                                override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                                    t.printStackTrace()
                                    DialogManager.openNotGoDalog(
                                        mContext,
                                        "이메일 신청 실패",
                                        t.message.toString()
                                    )
                                    LogUtil.logE("소유증서 이메일 발송 Fail.. ${t.message}")
                                }
                            })
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            DialogManager.openNotGoDalog(
                                mContext,
                                "이메일 신청 실패",
                                ex.message.toString()
                            )
                            LogUtil.logE("소유증서 이메일 발송시 알수없는 Error ! ${ex.message}")
                        }
                    }
                }

                // 이메일로 소유증서 신청 - jhm 2022/10/31
                binding.emailBtn.setOnClickListener {
                    LogUtil.logE("이메일로 받기 OnClick..")
                    if (PrefsHelper.read("email", "").isEmpty()) {
                        DialogManager.openTwoBtnDialog(
                            mContext,
                            "등록된 이메일이 없어요",
                            "이메일을 등록하고 소유증서를 메일로 받아보세요.",
                            failDialogEmailListener,
                            "이메일 등록"
                        )
                    } else {
                        DialogManager.openTwoBtnDialog(
                            mContext,
                            "이메일로 보내기",
                            "등록된 이메일로 소유증서를 직접 받아보실 수 있어요.",
                            postDialogEmailListener,
                            "이메일"
                        )
                    }

                }
            } else {
                val intent = Intent(applicationContext, NetworkActivity::class.java)
                startActivity(intent)
            }
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