package com.bsstandard.piece.view.alarm

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
import androidx.lifecycle.ViewModelProvider
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.AlarmViewModel
import com.bsstandard.piece.data.viewmodel.PutAlarmViewModel
import com.bsstandard.piece.databinding.ActivityAlarmBinding
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *packageName    : com.bsstandard.piece.view.alarm
 * fileName       : AlarmActivity
 * author         : piecejhm
 * date           : 2022/10/16
 * description    : 알림 및 혜택 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/16        piecejhm       최초 생성
 */

@AndroidEntryPoint
class AlarmActivity : BaseActivity<ActivityAlarmBinding>(R.layout.activity_alarm) {
    private lateinit var avm: AlarmViewModel // 알림 및 혜택 조회 ViewModel - jhm 2022/10/16
    private val pvm by viewModels<PutAlarmViewModel>() // 사용자 알림 읽음 처리 ViewModel - jhm 2022/10/17

    val mContext: Context = this@AlarmActivity
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            avm = ViewModelProvider(this@AlarmActivity)[AlarmViewModel::class.java]

            binding.alarmViewModel = avm
            binding.lifecycleOwner = this@AlarmActivity

            // 사용자 알림 읽음 처리 ViewModel Binding - jhm 2022/10/17
            putAlarmViewModel = pvm


            // UI Setting 최종 - jhm 2022/09/14
            setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
            setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
            setNaviBarIconColor(true) // 네비게이션 true : 검정색
            setNaviBarBgColor("#ffffff") // 네비게이션 배경색
        }


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {
                pvm.putAlaram()
                pvm.putAlarmResponse.observe(this@AlarmActivity, Observer {
                    LogUtil.logE("알림 읽음처리 상태값 : " + it.message)
                    avm.getAlarmList(
                        accessToken = "Bearer $accessToken",
                        deviceId = deviceId,
                        memberId = memberId,
                        100,
                        "NTT01"
                    )
                })

                // 알림 및 혜택 목록 조회 요청 - jhm 2022/10/16
                avm.getAlarmList(
                    accessToken = "Bearer $accessToken",
                    deviceId = deviceId,
                    memberId = memberId,
                    100,
                    "NTT01"
                )

                if (avm.alarmResponse.value?.data?.totalCount == 0) {
                    binding.noticeLayout.visibility = View.VISIBLE
                    binding.eventLayout.visibility = View.GONE
                    binding.scrollLayout.visibility = View.GONE
                } else {
                    binding.noticeLayout.visibility = View.GONE
                    binding.eventLayout.visibility = View.GONE
                    binding.scrollLayout.visibility = View.VISIBLE
                    avm.viewInit(binding.alarmRv)
                }


                // 알림 / 혜택 OnClick tab 기능 - jhm 2022/10/16
                binding.noticeTitle.setOnClickListener {
                    LogUtil.logE("알림 탭 OnClick..")

                    binding.benefitTitle.setTextColor(mContext.getColor(R.color.c_dadce3))
                    binding.noticeTitle.setTextColor(mContext.getColor(R.color.c_131313))

                    GlobalScope.launch {
                        avm.getAlarmList(
                            accessToken = "Bearer $accessToken",
                            deviceId = deviceId,
                            memberId = memberId,
                            100,
                            "NTT01"
                        )

                        this@AlarmActivity.runOnUiThread {
                            if (avm.alarmResponse.value?.data?.totalCount == 0) {
                                binding.noticeLayout.visibility = View.VISIBLE
                                binding.eventLayout.visibility = View.GONE
                                binding.scrollLayout.visibility = View.GONE
                            } else {
                                binding.noticeLayout.visibility = View.GONE
                                binding.eventLayout.visibility = View.GONE
                                binding.scrollLayout.visibility = View.VISIBLE
                                avm.viewInit(binding.alarmRv)
                            }
                        }
                    }
                }



                binding.benefitTitle.setOnClickListener {
                    LogUtil.logE("혜택 탭 OnClick..")
                    binding.benefitTitle.setTextColor(mContext.getColor(R.color.c_131313))
                    binding.noticeTitle.setTextColor(mContext.getColor(R.color.c_dadce3))



                    GlobalScope.launch {
                        avm.getAlarmList(
                            accessToken = "Bearer $accessToken",
                            deviceId = deviceId,
                            memberId = memberId,
                            100,
                            "NTT02"
                        )

                        this@AlarmActivity.runOnUiThread {

                            if (avm.alarmResponse.value?.data?.alarms == null) {
                                LogUtil.logE("여기 타닝?")
                                binding.noticeLayout.visibility = View.GONE
                                binding.eventLayout.visibility = View.VISIBLE
                                binding.scrollLayout.visibility = View.GONE
                            } else {
                                LogUtil.logE("여기타닝?2")
                                binding.noticeLayout.visibility = View.GONE
                                binding.eventLayout.visibility = View.GONE
                                binding.scrollLayout.visibility = View.VISIBLE
                                avm.viewInit(binding.alarmRv)
                            }

                        }
                    }
                }

                binding.closeBtn.setOnClickListener {
                    LogUtil.logE("화면 닫기 OnClick..")
                    finish()
                }

            } else {
                val intent = Intent(applicationContext, NetworkActivity::class.java)
                startActivity(intent)
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