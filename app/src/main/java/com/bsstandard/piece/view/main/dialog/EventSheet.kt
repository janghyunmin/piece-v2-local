package com.bsstandard.piece.view.main.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bsstandard.piece.R
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.databinding.SlideupEventBinding
import com.bsstandard.piece.widget.utils.LogUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

/**
 *packageName    : com.bsstandard.piece.view.main.dialog
 * fileName       : EventSheet
 * author         : piecejhm
 * date           : 2022/07/07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/07        piecejhm       최초 생성
 */

class EventSheet() : BottomSheetDialogFragment() {

    lateinit var binding: SlideupEventBinding;
    private var toDay : String = "false";


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
        val view = View.inflate(context, R.layout.slideup_event, null);

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
                    dismiss()

                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // do on slide
            }
        })

        toDay = PrefsHelper.read("toDay","false")

        binding.today.setOnClickListener { toDayDismiss() }
        binding.close.setOnClickListener { onDismiss() }

        return dialog
    }

    // 오늘은 보지 않기 이벤트 - jhm 2022/07/07
    fun toDayDismiss() {
        LogUtil.logE("오늘은 보지 않기 onClick..")
        PrefsHelper.write("toDayChk","true")
        dismiss()
    }

    // 닫기 이벤트 - jhm 2022/07/07
    fun onDismiss() {
        LogUtil.logE("닫기 onClick..")
        dismiss()
    }
}