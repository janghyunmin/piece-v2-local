package com.bsstandard.piece.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bsstandard.piece.R
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.GetUserViewModel
import com.bsstandard.piece.data.viewmodel.MoreViewModel
import com.bsstandard.piece.databinding.FragmentMoreBinding
import com.bsstandard.piece.view.certification.CertificationActivity
import com.bsstandard.piece.view.consent.MoreConsentActivity
import com.bsstandard.piece.view.coupon.CouponActivity
import com.bsstandard.piece.view.event.EventActivity
import com.bsstandard.piece.view.myInfo.MyInfoActivity
import com.bsstandard.piece.view.notice.NoticeActivity
import com.bsstandard.piece.view.notisetting.NotificationSettingActivity
import com.bsstandard.piece.view.question.QuestionActivity
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

/**
 *packageName    : com.bsstandard.piece.view.fragment
 * fileName       : FragmentMore
 * author         : piecejhm
 * date           : 2022/07/08
 * description    : 더보기 탭
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/08        piecejhm       최초 생성
 */


@AndroidEntryPoint
class FragmentMore : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!
    private lateinit var vm: MoreViewModel
    private var disposable: Disposable? = null

    var getUserViewModel: GetUserViewModel? = null

    companion object {
        fun newInstance(): FragmentMore {
            return FragmentMore()
        }
    }

    // 프래그먼트를 포함하고 있는 액티비티에 붙었을 때 - jhm 2022/07/15
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    // 메모리 누수 방지를 위해 자원 해제 - jhm 2022/08/08
    override fun onDestroy() {
        super.onDestroy()
        disposable?.let { disposable!!.dispose() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false)
//        requireActivity().setStatusBarTransparent()
//        getStatusBarHeight(requireContext())
//        binding.pLayout.setPadding(0, 0, 0, 0)

//        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        (activity as AppCompatActivity).supportActionBar?.hide()

        vm = MoreViewModel()
        binding.moreViewModel = vm
        binding.lifecycleOwner = viewLifecycleOwner

        // 내정보 API - jhm 2022/09/03
        getUserViewModel = ViewModelProvider(this)[GetUserViewModel::class.java]
        getUserViewModel!!.getUserData()

        binding.userName.text = PrefsHelper.read("name", "") + " 님"
        binding.dateText.text = PrefsHelper.read("joinDay", "") + "일"
        binding.version.text = "v." + PrefsHelper.read("appVersion","")


        myInfo() // 내정보 상세 - jhm 2022/09/11
        goNotice() // 공지사항 - jhm 2022/09/11
        goEvent() // 이벤트 - jhm 2022/09/11
        goCoupon() // 쿠폰함 - jhm 2022/09/13
        goAccess() // 인증 및 보안 - jhm 2022/09/15
        goNotiSetting() // 알림 설정 - jhm 2022/09/21
        goConsent() // 약관 및 개인정보 처리 등 동의 - jhm 2022/09/22
        goQuestion() // 자주 묻는 질문 - jhm 2022/09/23

        return binding.root
    }

    fun Activity.setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = 0x00000000  // transparent
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.addFlags(flags)
        }
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    // 이름 클릭시 내정보 페이지로 이동 - jhm 2022/09/01
    private fun myInfo() {
        vm.startMyInfoDetail.observe(
            viewLifecycleOwner
        ) {
            LogUtil.logE("onclick..")
            val intent = Intent(context, MyInfoActivity::class.java)
            activity?.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
            context?.startActivity(intent)

        }
    }


    // 공지사항 - jhm 2022/09/10
    private fun goNotice() {
        vm.startNotice.observe(
            viewLifecycleOwner
        ) {
            LogUtil.logE("공지사항 OnClick..")
            val intent = Intent(context, NoticeActivity::class.java)
            context?.startActivity(intent)
            activity?.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
        }
    }

    // 이벤트 - jhm 2022/09/11
    private fun goEvent() {
        vm.startEvent.observe(
            viewLifecycleOwner
        ) {
            LogUtil.logE("이벤트 OnClick..")
            val intent = Intent(context, EventActivity::class.java)
            context?.startActivity(intent)
            activity?.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
        }
    }

    // 쿠폰함 - jhm 2022/09/13
    private fun goCoupon() {
        vm.startCoupon.observe(
            viewLifecycleOwner
        ) {
            LogUtil.logE("쿠폰함 OnClick..")
            val intent = Intent(context, CouponActivity::class.java)
            context?.startActivity(intent)
            activity?.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
        }
    }

    // 인증 및 보안 - jhm 2022/09/15
    private fun goAccess() {
        vm.startAccess.observe(
            viewLifecycleOwner
        ) {
            LogUtil.logE("인증 및 보안 OnClick..")
            val intent = Intent(context, CertificationActivity::class.java)
            activity?.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
            context?.startActivity(intent)

        }
    }

    // 알림 및 설정 - jhm 2022/09/21
    private fun goNotiSetting() {
        vm.startNotiSetting.observe(
            viewLifecycleOwner
        ) {
            LogUtil.logE("알림 설정 OnClick..")
            val intent = Intent(context, NotificationSettingActivity::class.java)
            activity?.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
            context?.startActivity(intent)
        }
    }

    // 약관 및 개인정보 처리 등 동의 - jhm 2022/09/22
    private fun goConsent() {
        vm.startConsent.observe(
            viewLifecycleOwner
        ) {
            LogUtil.logE("약관 및 개인정보 처리 등 동의")
            val intent = Intent(context, MoreConsentActivity::class.java)
            activity?.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
            context?.startActivity(intent)
        }
    }

    // 자주 묻는 질문 - jhm 2022/09/23
    private fun goQuestion() {
        vm.startQuestion.observe(
            viewLifecycleOwner
        ) {
            LogUtil.logE("자주 묻는 질문")
            val intent = Intent(context, QuestionActivity::class.java)
            activity?.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
            context?.startActivity(intent)
        }
    }


    // statusbar height 추가
    open fun getStatusBarHeight(context: Context): Int {
        val screenSizeType = context.resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK
        var statusbar = 0
        if (screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            val resourceId =
                context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusbar = context.resources.getDimensionPixelSize(resourceId)
            }
        }
        return statusbar
    }

}