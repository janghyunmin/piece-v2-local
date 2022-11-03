package com.bsstandard.piece.widget.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.bsstandard.piece.R
import com.bsstandard.piece.databinding.*

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
    /**
     * 버튼 Type 1개
     * Activity 종료 포함
     * **/
    fun openDalog(
        context: Context,
        title: String,
        subTitle: String,
        btnTitle: String,
        activity: Activity
    ) {
        val dialog = Dialog(context, R.style.Dialog)
        val dialogBinding = DialogBasicBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.title.text = title
        dialogBinding.subTitle.text = subTitle
        dialogBinding.okBtn.text = btnTitle

        dialogBinding.okBtn.setOnClickListener {
            dialog.dismiss()
            activity.finish()
        }

        dialog.show()
    }

    /**
     * 버튼 Type 1개
     * Activity 종료 미포함
     * 유의사항 및 텍스트 Left 정렬
     * **/
    fun openLeftDalog(
        context: Context,
        title: String,
        subTitle: String,
        btnTitle: String
    ) {
        val dialog = Dialog(context, R.style.Dialog)
        val dialogBinding = DialogTextLeftBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.title.text = title
        dialogBinding.subTitle.text = subTitle
        dialogBinding.okBtn.text = btnTitle

        dialogBinding.okBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    /**
     * 버튼 Type 1개
     * Activity 종료 미포함
     * **/
    fun openNotGoDalog(
        context: Context,
        title: String,
        subTitle: String,
    ) {
        val dialog = Dialog(context, R.style.Dialog)
        val dialogBinding = DialogBasicBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.title.text = title
        dialogBinding.subTitle.text = subTitle

        dialogBinding.okBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }




    /**
     * 버튼 Type 1개
     * 버튼 클릭시 업데이트 이동
     * **/
    fun openGoUpdate(
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
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(context.resources.getString(R.string.playstore_url))
            context.startActivity(intent)
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

            "쿠폰 사용" -> {
                dialogBinding.cancleBtn.text = "닫기"
                dialogBinding.okBtn.text = "사용하기"
            }
            "등록된 계좌" -> {
                dialogBinding.cancleBtn.text = "뒤로"
                dialogBinding.okBtn.text = "계좌 등록"
            }
            "탈퇴 불가" -> {
                dialogBinding.cancleBtn.text = "탈퇴 취소"
                dialogBinding.okBtn.text = "카카오톡 문의하기"
            }
            "로그아웃" -> {
                dialogBinding.cancleBtn.text = "취소"
                dialogBinding.okBtn.text = "로그아웃"
            }
            "주소 등록" -> {
                dialogBinding.cancleBtn.text = "뒤로"
                dialogBinding.okBtn.text = "주소 등록"
            }
            "우편" -> {
                dialogBinding.cancleBtn.text = "뒤로"
                dialogBinding.okBtn.text = "우편으로 받기"
            }
            "이메일 등록" -> {
                dialogBinding.cancleBtn.text = "뒤로"
                dialogBinding.okBtn.text = "이메일 등록"
            }
            "이메일" -> {
                dialogBinding.cancleBtn.text = "뒤로"
                dialogBinding.okBtn.text = "이메일 신청"
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
            "회원 탈퇴" -> {
                dialogBinding.cancleBtn.text = "취소"
                dialogBinding.okBtn.text = "탈퇴"
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