package com.bsstandard.piece.view.fragment.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bsstandard.piece.R
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.databinding.SlideupProfitBinding
import com.bsstandard.piece.view.authentication.AuthenticationActivity
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.DecimalFormat

/**
 *packageName    : com.bsstandard.piece.view.fragment.dialog
 * fileName       : ProfitBottomSheetDialog
 * author         : piecejhm
 * date           : 2022/10/26
 * description    : 분배금 정산 알림 BottomSheetDialog
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/26        piecejhm       최초 생성
 */


class ProfitBottomSheetDialog() : BottomSheetDialogFragment() {
    lateinit var binding: SlideupProfitBinding;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog;
        val view = View.inflate(context, R.layout.slideup_profit, null);

        binding = DataBindingUtil.bind(view)!!;

        dialog.setContentView(view);

        var bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View);
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        //bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    // do on STATE_EXPANDED
                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    // do on STATE_COLLAPSED
                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {

                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // do on slide
            }
        })

        Glide.with(requireContext()).load(R.raw.withdraw_complete_lopping).into(binding.lottie)

        var totalProfitAmount = arguments?.getString("totalProfitAmount").toString()
        LogUtil.logE("넘겨받은 수익금 잔액 $totalProfitAmount")

        val decimal = DecimalFormat("#,###")
        var depositText : String = ""

        depositText = decimal.format(totalProfitAmount.toInt())
        // 분배금 전환 금액 - jhm 2022/10/26
        binding.number.text = depositText

        binding.notice.text = "수익 분배금이 ${PrefsHelper.read("name","")}님을 기다리고 있어요! \n 실명인증을 하시면 예치금으로 입금해 드려요."


        // 지금 실명인증 하기 - jhm 2022/10/26
        binding.confirmBtn.setOnClickListener {
            LogUtil.logE("실명인증 OnClick..")
            dismiss()


            val intent = Intent(context, AuthenticationActivity::class.java)
            activity?.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
            startActivity(intent)
        }

        return dialog
    }
}