package com.bsstandard.piece.widget.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.bsstandard.piece.R
import com.bsstandard.piece.databinding.AddressDialogBinding
import com.bsstandard.piece.databinding.CDialogBinding
import com.bsstandard.piece.databinding.CDialogPasswordBinding

/**
 *packageName    : com.bsstandard.piece.widget.utils
 * fileName       : CustomDialog
 * author         : piecejhm
 * date           : 2022/07/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/04        piecejhm       최초 생성
 */


class CustomDialog(
    context: Context,
    private val division: String,
    private val listener_c: CustomDialogListener,
    private val listener_p: CustomDialogPassCodeListener
) : Dialog(context) {

    // 업데이트 알림 Dialog binding
    private lateinit var cDialogBinding: CDialogBinding

    // 비밀번호 재설정 Dialog binding
    private lateinit var cDialogPasswordBinding: CDialogPasswordBinding

    // 주소 등록 완료 Dialog binding - jhm 2022/09/07
    private lateinit var addressDialogBinding: AddressDialogBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        when (division) {
            "version" -> {
                LogUtil.logE("version Dialog 호출")
                cDialogBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.c_dialog,
                    null, false
                )

                setContentView(cDialogBinding.root)
                // 다이얼로그 배경 제거
                window!!.setDimAmount(0f)
                // 다이얼로그 사이즈 조절
                context.dialogResize(this, 1f, 0.3f)

                cDialogBinding.apply {
                    // 버튼 클릭 시 리스너 메소드 호출
                    okBtn.setOnClickListener {
                        listener_c.onOkButtonClicked()
                    }
                }
            }


            "password" -> {
                LogUtil.logE("password Dialog 호출")
                cDialogPasswordBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.c_dialog_password,
                    null, false
                )
                setContentView(cDialogPasswordBinding.root)

                // 다이얼로그 사이즈 조절
                context.dialogResize(this, 1f, 0.3f)

                cDialogPasswordBinding.apply {
                    cancleBtn.setOnClickListener {
                        listener_p.onCancleButtonClicked()
                    }

                    rePassword.setOnClickListener {
                        listener_p.onRetryPassCodeButtonClicked()
                    }
                }

            }

            "address" -> {
                LogUtil.logE("주소 등록 완료 Dialog 호출")
                addressDialogBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.address_dialog,
                    null, false
                )

                setContentView(addressDialogBinding.root)

                // 다이얼로그 사이즈 조절
                context.dialogResize(this, 1f, 0.3f)

                addressDialogBinding.apply {
                    okBtn.setOnClickListener {
                        listener_c.onOkButtonClicked()
                    }
                }
            }
        }

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 취소 불가능
        setCancelable(false)
    }
}