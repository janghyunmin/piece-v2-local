package com.bsstandard.piece.view.join

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bsstandard.piece.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.popup_slideup.view.*

/**
 *packageName    : com.bsstandard.piece.ui.join
 * fileName       : JoinBottomDialog
 * author         : piecejhm
 * date           : 2022/05/06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/06        piecejhm       최초 생성
 */


class JoinBottomDialog(val itemClick: (Int) -> Unit) :
    BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.popup_slideup,
            container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.skt_tv.setOnClickListener {
            itemClick(0)
            view.skt_tv.setTextColor(resources.getColor(R.color.black))
            dialog?.dismiss()
        }
        view.kt_tv.setOnClickListener {
            itemClick(1)
            view.kt_tv.setTextColor(resources.getColor(R.color.black))
            dialog?.dismiss()
        }
        view.lg_tv.setOnClickListener {
            itemClick(2)
            dialog?.dismiss()
        }
        view.skt_sub_tv.setOnClickListener {
            itemClick(3)
            dialog?.dismiss()
        }
        view.kt_sub_tv.setOnClickListener {
            itemClick(4)
            dialog?.dismiss()
        }
        view.lg_sub_tv.setOnClickListener {
            itemClick(5)
            dialog?.dismiss()
        }
    }


}