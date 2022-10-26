package com.bsstandard.piece.view.question

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.viewmodel.BoardViewModel
import com.bsstandard.piece.databinding.ActivityQuestionBinding
import com.bsstandard.piece.view.adapter.question.QuestionAdapter
import com.bsstandard.piece.widget.utils.LogUtil
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

/**
 *packageName    : com.bsstandard.piece.view.question
 * fileName       : QuestionActivity
 * author         : piecejhm
 * date           : 2022/09/23
 * description    : 더보기 - 자주 묻는 질문 Activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/23        piecejhm       최초 생성
 */


@AndroidEntryPoint
class QuestionActivity : BaseActivity<ActivityQuestionBinding>(R.layout.activity_question) {
    private lateinit var vm: BoardViewModel
    private val mContext: Context = this@QuestionActivity


    companion object {
        const val TAG: String = "QuestionActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProvider(this@QuestionActivity)[BoardViewModel::class.java]

        binding.questionVm = vm
        binding.lifecycleOwner = this@QuestionActivity

        // UI Setting 최종 - jhm 2022/09/14
        setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
        setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
        setNaviBarIconColor(true) // 네비게이션 true : 검정색
        setNaviBarBgColor("#ffffff") // 네비게이션 배경색



        // 전체 자주묻는 질문 List  - jhm 2022/09/23
        viewInit()






        binding.tabs.addTab(binding.tabs.newTab().setText("전체"))
        binding.tabs.addTab(binding.tabs.newTab().setText("회원"))
        binding.tabs.addTab(binding.tabs.newTab().setText("구매"))
        binding.tabs.addTab(binding.tabs.newTab().setText("분배"))
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                LogUtil.logE("탭 재선택..")
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                LogUtil.logE("탭 선택 : + ${tab?.contentDescription}")
                when(tab?.contentDescription) {
                    "전체" -> {
                        vm.getQuestion("","BRT03" , 31 , 1)
                        LogUtil.logE("전체 탭 선택")
                    }
                    "회원" -> {
                        vm.getQuestion("BRT0301","BRT03" ,10 , 1)
                        LogUtil.logE("회원 탭 선택")
                    }
                    "구매" -> {
                        vm.getQuestion("BRT0302","BRT03" ,10 , 1)
                        LogUtil.logE("구매 탭 선택")
                    }
                    "분배" -> {
                        vm.getQuestion("BRT0303","BRT03" ,10 , 1)
                        LogUtil.logE("분배 탭 선택")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                LogUtil.logE("탭 선택 안함 + $tab")
            }
        })
    }

    // 자주 묻는 질문 adapter viewInit - jhm 2022/09/28
    private fun viewInit() {
        vm.viewInit(binding.questionRv,"자주 묻는 질문")
        vm.getQuestion("","BRT03" , 31 , 1)

        vm.questionItemList.observe(this@QuestionActivity, Observer {
            LogUtil.logE("item count : " + vm.getQuestionItem().size)
            vm.viewInit(binding.questionRv,"자주 묻는 질문")
        })

        vm.questionAdapter.setOnItemClickListener(object : QuestionAdapter.OnItemClickListener {
            override fun onItemClick(v: View, title: String, contents: String) {

            }
        })
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