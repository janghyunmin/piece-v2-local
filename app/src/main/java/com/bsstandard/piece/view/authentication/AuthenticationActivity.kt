package com.bsstandard.piece.view.authentication

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datamodel.dmodel.authentication.CallUsernameAuthModel
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.BaseDTO
import com.bsstandard.piece.databinding.ActivityAuthenticationBinding
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.widget.utils.DialogManager
import com.bsstandard.piece.widget.utils.KeyboardVisibilityUtils
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 *packageName    : com.bsstandard.piece.view.authentication
 * fileName       : AuthenticationActivity
 * author         : piecejhm
 * date           : 2022/10/26
 * description    : 실명인증하기 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/26        piecejhm       최초 생성
 */

@AndroidEntryPoint
class AuthenticationActivity :
    BaseActivity<ActivityAuthenticationBinding>(R.layout.activity_authentication) {
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    private val viewModel by viewModels<AuthInputViewModel>()

    // 주민등록번호 실명인증 요청 API - jhm 2022/10/26
    val apiResponse: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    var mContext: Context = this@AuthenticationActivity
    var mFirst: String = ""
    var mLast: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // UI Setting 최종 - jhm 2022/09/14
        setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
        setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
        setNaviBarIconColor(true) // 네비게이션 true : 검정색
        setNaviBarBgColor("#ffffff") // 네비게이션 배경색


        keyboardVisibilityUtils =
            KeyboardVisibilityUtils(window, onShowKeyboard = { keyboardHeight ->
                sv_root.run {
                    smoothScrollTo(scrollX, scrollY + keyboardHeight)
                }
            })




        binding.apply {
            lifecycleOwner = this@AuthenticationActivity
            binding.lifecycleOwner = this@AuthenticationActivity
            binding.auth = this@AuthenticationActivity
            authViewModel = viewModel



            binding.closeIcon.setOnClickListener {
                finish()
            }

            binding.name.text = PrefsHelper.read("name", "")

            val userFirstObserver =
                Observer { userFirstObserver: String ->
                    mFirst = userFirstObserver
                }
            val userLastObserver =
                Observer { userLastObserver: String ->
                    mLast = userLastObserver
                }


            authViewModel?.getFirst()?.observe(this@AuthenticationActivity, userFirstObserver)
            authViewModel?.getFirst()?.observe(this@AuthenticationActivity, Observer {
                mFirst = it
                onNext()
                LogUtil.logE("첫번째 6자리 주민번호 입력 $it")
            })


            authViewModel?.getLast()?.observe(this@AuthenticationActivity, userLastObserver)
            authViewModel?.getLast()?.observe(this@AuthenticationActivity, Observer {
                mLast = it
                onNext()
                LogUtil.logE("남은 7자리 주민번호 입력 $it")
            })
        }
    }


    private fun onNext() {
        LogUtil.logE("mFirst : ${mFirst.length}")
        LogUtil.logE("mLast : ${mLast.length}")
        if (mFirst.length == 6 && mLast.length == 7) {
            LogUtil.logE("버튼 활성화")
            binding.authBtn.isSelected = true
            binding.authBtn.setOnClickListener {
                apiResponse?.postUserNameAuth(
                    callUsernameAuthModel = CallUsernameAuthModel(
                        PrefsHelper.read("memberId", ""),
                        PrefsHelper.read("name", ""),
                        mFirst + mLast,
                        "Y",
                        "Y"
                    )
                )?.enqueue(object : Callback<BaseDTO> {
                    override fun onResponse(call: Call<BaseDTO>, response: Response<BaseDTO>) {
                        try {
                            LogUtil.logE("주민등록번호 실명인증 요청 API Call Success")
                            if(response.code() == 200) {
                                LogUtil.logE("실명인증 완료")
                                apiResponse.postDepositBalance(
                                    accessToken, deviceId, memberId
                                ).enqueue(object : Callback<BaseDTO> {
                                    override fun onResponse(
                                        call: Call<BaseDTO>,
                                        response: Response<BaseDTO>
                                    ) {
                                        try {
                                            LogUtil.logE("고객 분배금 예치금 계좌 입금 완료")
                                            LogUtil.logE("try: ${response.body()?.message}")
                                            if(response.code() == 200) {
                                                DialogManager.openDalog(mContext,"실명 인증 완료","분배금이 예치금으로 입금되었어요.",this@AuthenticationActivity)
                                            }
                                            else {
                                                DialogManager.openDalog(mContext,"분배금 전환에 실패했어요!","분배금이 존재하지 않습니다.",this@AuthenticationActivity)
                                            }
                                        } catch (ex: Exception) {
                                            ex.printStackTrace()
                                            LogUtil.logE("분배금 예치금 전환 API Fail 시스템 오류..")
                                        }
                                    }

                                    override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                                        t.printStackTrace()
                                        LogUtil.logE("분배금 예치금 전환 API Call Fail.." + t.message)
                                    }
                                })
                            }
                            else {
                                DialogManager.openDalog(mContext,"실명 인증에 실패했어요!","주민등록번호를 정확하게 입력한 후 \n다시 시도해 주세요.",this@AuthenticationActivity)
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            LogUtil.logE("실명인증 Fail..")
                            DialogManager.openDalog(mContext,"실명 인증에 실패했어요!","주민등록번호를 정확하게 입력한 후 \n다시 시도해 주세요.",this@AuthenticationActivity)
                        }
                    }

                    override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                        t.printStackTrace()
                        LogUtil.logE("주민등록번호 실명인증 요청 API Call Fail..")
                    }
                })
            }
        } else {
            LogUtil.logE("버튼 비활성화")
            binding.authBtn.isSelected = false
        }
    }

    override fun onDestroy() {
        keyboardVisibilityUtils.detachKeyboardListeners()
        super.onDestroy()
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