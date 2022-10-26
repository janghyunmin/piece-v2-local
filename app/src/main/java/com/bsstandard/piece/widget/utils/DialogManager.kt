package com.bsstandard.piece.widget.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.bsstandard.piece.R
import com.bsstandard.piece.databinding.CertificationOffDialogBinding
import com.bsstandard.piece.databinding.DialogBasicBinding
import com.bsstandard.piece.databinding.DialogCancleBinding
import com.bsstandard.piece.databinding.DialogMiddleBinding

/**
 *packageName    : com.bsstandard.piece.widget.utils
 * fileName       : DialogManager
 * author         : piecejhm
 * date           : 2022/10/18
 * description    : 공통 Dialog
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/18        piecejhm       최초 생성
 */

object DialogManager {
    // 버튼 타입 1개 , activity 종료 포함 - jhm 2022/10/18
    fun openDalog(
        context: Context,
        title: String,
        subTitle: String,
        activity: Activity
    ) {
        val dialog = Dialog(context, R.style.Dialog)
        val dialogBinding = DialogBasicBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.title.text = title
        dialogBinding.subTitle.text = subTitle

        dialogBinding.okBtn.setOnClickListener {
            dialog.dismiss()
            activity.finish()
        }

        dialog.show()
    }



    // 버튼 타입 2개 - jhm 2022/10/18
    fun openTwoBtnDialog(
        context: Context,
        title: String,
        subTitle: String,
        listener_c: CustomDialogListener,
        type: String
    ) {
        val dialog = Dialog(context, R.style.Dialog)
        val dialogBinding = DialogMiddleBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.title.text = title
        dialogBinding.subTitle.text = subTitle

        when (type) {
            "비밀번호 재설정" -> {
                dialogBinding.cancleBtn.text = "뒤로 가기"
                dialogBinding.okBtn.text = "비밀번호 재설정"
            }

            "구매 확정" -> {
                dialogBinding.cancleBtn.text = "취소"
                dialogBinding.okBtn.text = "확인"
            }


        }

        // 취소 및 닫기 - jhm 2022/10/18
        dialogBinding.cancleBtn.setOnClickListener {
            listener_c.onCancelButtonClicked()
            dialog.dismiss()
        }

        // 확인 - jhm 2022/10/18
        dialogBinding.okBtn.setOnClickListener {
            listener_c.onOkButtonClicked()
            dialog.dismiss()
        }

        dialog.show()
    }

    // 버튼 타입 2개 c_ff7878 - jhm 2022/10/25
    fun openTwoBtnNagativeDialog(
        context: Context,
        title: String,
        subTitle: String,
        listener_c: CustomDialogListener,
        type: String
    ) {
        val dialog = Dialog(context, R.style.Dialog)
        val dialogBinding = DialogCancleBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.title.text = title
        dialogBinding.subTitle.text = subTitle

        when (type) {
           "구매 취소" -> {
               dialogBinding.cancleBtn.text = "아니요"
               dialogBinding.okBtn.text = "구매 취소"
           }
        }

        // 취소 및 닫기 - jhm 2022/10/18
        dialogBinding.cancleBtn.setOnClickListener {
            listener_c.onCancelButtonClicked()
            dialog.dismiss()
        }

        // 확인 - jhm 2022/10/18
        dialogBinding.okBtn.setOnClickListener {
            listener_c.onOkButtonClicked()
            dialog.dismiss()
        }

        dialog.show()
    }

    // 버튼 타입 2개 생체인증 - jhm 2022/10/18
    @SuppressLint("ResourceAsColor")
    fun openAuthDialog(
        context: Context,
        title: String,
        subTitle: String,
        listener_c: CustomDialogListener,
        type: String,
        boolean: Boolean
    ) {
        val dialog = Dialog(context, R.style.Dialog)
        when (type) {
            "생체인증 등록" -> {
                val dialogBinding = DialogMiddleBinding.inflate(dialog.layoutInflater)
                dialog.setContentView(dialogBinding.root)

                dialogBinding.title.text = title
                dialogBinding.subTitle.text = subTitle

                dialogBinding.cancleBtn.text = "뒤로"
                dialogBinding.okBtn.text = "등록"

                // 확인 - jhm 2022/10/18
                dialogBinding.okBtn.setOnClickListener {
                    listener_c.onOkButtonClicked()
                    dialog.dismiss()
                }

                // 취소 및 닫기 - jhm 2022/10/18
                dialogBinding.cancleBtn.setOnClickListener {
                    listener_c.onCancelButtonClicked()
                    dialog.dismiss()
                }

            }
            "생체인증 해제" -> {
                val dialogBinding = CertificationOffDialogBinding.inflate(dialog.layoutInflater)
                dialog.setContentView(dialogBinding.root)

                dialogBinding.title.text = title
                dialogBinding.subTitle.text = subTitle


                dialogBinding.cancleBtn.text = "확인"
                dialogBinding.okBtn.text = "취소"

                // 확인 - jhm 2022/10/18
                dialogBinding.okBtn.setOnClickListener {
                    listener_c.onOkButtonClicked()
                    dialog.dismiss()
                }

                // 취소 및 닫기 - jhm 2022/10/18
                dialogBinding.cancleBtn.setOnClickListener {
                    listener_c.onCancelButtonClicked()
                    dialog.dismiss()
                }

            }
        }
        dialog.show()


    }

}