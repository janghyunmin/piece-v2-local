package com.bsstandard.piece.view.virtual

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
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.OccupationDTO
import com.bsstandard.piece.data.viewmodel.ConsentViewModel
import com.bsstandard.piece.databinding.ActivityVirtualAccountBinding
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.view.adapter.consent.ConsentAdapter
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.view.webview.ConsentDetailWebView
import com.bsstandard.piece.widget.utils.Division
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *packageName    : com.bsstandard.piece.view.virtual
 * fileName       : VirtualActivity
 * author         : piecejhm
 * date           : 2022/10/05
 * description    : 가상계좌 생성 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/05        piecejhm       최초 생성
 */


@AndroidEntryPoint
class VirtualActivity :
    BaseActivity<ActivityVirtualAccountBinding>(R.layout.activity_virtual_account) {

    private val context: Context = this@VirtualActivity
    private val consentViewModel by viewModels<ConsentViewModel>() // consentViewModel - jhm 2022/09/21
    private var disposable: Disposable? = null

    private var consentAdapter: ConsentAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var consentList: ArrayList<ConsentList?>? =
        ArrayList() // 약관 adapter 연결 List - jhm 2022/09/22
    var chkCnt = 4

    // 가상계좌 생성시 인증번호 API - jhm 2022/10/06
    val response: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")


    var virtualSmsDialog: VirtualSmsDialog? = null
    var chargeMoney: String = ""

    companion object {
        const val TAG: String = "VirtualActivity"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // UI Setting 최종 - jhm 2022/09/14
        setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
        setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
        setNaviBarIconColor(true) // 네비게이션 true : 검정색
        setNaviBarBgColor("#ffffff") // 네비게이션 배경색


        binding.apply {
            binding.lifecycleOwner = this@VirtualActivity
            mvConsent = consentViewModel

        }

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {

                consentViewModel.getConsentData("SETTLE")
                    .observe(this@VirtualActivity, Observer {
                        LogUtil.logE("약관 목록 조회")

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
                                    it.data[index].isMandatory,
                                    Division.SETTLE_CONSET
                                )
                            )
                        }

                        // 약관 목록 UI - jhm 2022/09/22
                        ConsentUI()
                    })
            } else {
                val intent = Intent(applicationContext, NetworkActivity::class.java)
                startActivity(intent)
            }

            var intent = intent
            chargeMoney = intent.getStringExtra("chargeMoney").toString()
            LogUtil.logE("넘겨받은 충전할 금액 값 : $chargeMoney")


            // 사용자 휴대폰 번호 - jhm 2022/10/06
            binding.phone.text = PrefsHelper.read("cellPhoneNo", "")
        }
    }

    private fun ConsentUI() {
        consentAdapter = ConsentAdapter(context, consentList)
        layoutManager = LinearLayoutManager(context)
        binding.consentRV.layoutManager = layoutManager
        binding.consentRV.setHasFixedSize(true)
        binding.consentRV.adapter = consentAdapter

        binding.allChk.isChecked = false
        setEnabledBtn(0)


        // 전체 체크박스 클릭시 - jhm 2022/10/06
        binding.allChk.setOnClickListener {
            if (binding.allChk.isChecked) {
                LogUtil.logE("전체 체크박스 클릭")
                consentAdapter!!.selectAll()
                setEnabledBtn(4)
            } else {
                consentAdapter!!.unselectAll()
                setEnabledBtn(0)
            }
        }

        // 필수 개별 체크시 모두동의 해제 및 선택 로직 - jhm 2022/10/06
        consentAdapter!!.setOnClickListener {
            if (it) chkCnt++
            else chkCnt--

            binding.allChk.isChecked = chkCnt == consentList?.size
            setEnabledBtn(chkCnt)
        }

        // 약관 상세웹뷰 화면 onClick - jhm 2022/06/21
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

    private fun setEnabledBtn(cnt: Int) {
        if (cnt == consentList?.size) {
            binding.confirmBtn.isSelected = true

            // 약관 동의 후 확인버튼 클릭 - jhm 2022/10/06
            binding.confirmBtn.setOnClickListener {
                // 인증번호 로직 실행 - jhm 2022/10/06
                LogUtil.logE("인증번호 로직 실행")

                response?.postOccupation(
                    "Bearer $accessToken",
                    deviceId = deviceId,
                    memberId = memberId
                )?.enqueue(object : Callback<OccupationDTO> {
                    override fun onResponse(
                        call: Call<OccupationDTO>,
                        response: Response<OccupationDTO>
                    ) {

                        try {
                            LogUtil.logE("휴대폰 점유 인증 성공 !")
                            val bundleData = Bundle()
                            bundleData.putString("chargeMoney", chargeMoney)
                            bundleData.putString(
                                "mchtTrdNo",
                                response.body()?.data?.mchtTrdNo.toString()
                            )
                            // 인증번호 입력하는 BottomSheetDialog 호출 - jhm 2022/10/06
                            virtualSmsDialog = VirtualSmsDialog(context)
                            virtualSmsDialog!!.isCancelable = false
                            virtualSmsDialog!!.arguments = bundleData
                            virtualSmsDialog!!.show(supportFragmentManager, Division.DIALOG_V_SMS)

                        } catch (ex: Exception) {
                            LogUtil.logE("휴대폰 점유 인증 실패 !")
                            ex.printStackTrace()
                        }
                    }

                    override fun onFailure(call: Call<OccupationDTO>, t: Throwable) {
                        LogUtil.logE("휴대폰 점유 인증 실패 !")
                    }
                })
            }

        } else {
            binding.confirmBtn.isSelected = false
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