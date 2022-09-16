package com.bsstandard.piece.widget.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.bsstandard.piece.R
import com.bsstandard.piece.databinding.*

/**
 *packageName    : com.bsstandard.piece.view.coupon
 * fileName       : CouponDialog
 * author         : piecejhm
 * date           : 2022/09/13
 * description    : 닫기 , 사용하기(확인) Dialog
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/13        piecejhm       최초 생성
 */


class CommonTwoTypeDialog(
    context: Context,
    private val division: String,
    private val listener_c: CustomDialogListener,
    private val message: String?,
    private val title: String?,
    private val subTitle: String?
) : Dialog(context) {

    // 쿠폰 사용 Dialog Dialog binding
    private lateinit var couponDialogBinding: CouponDialogBinding

    // 쿠폰 사용시 리턴 Dialog binding - jhm 2022/09/14
    private lateinit var couponResponseDialogBinding: CouponResponseDialogBinding

    // 쿠폰 유의사항 Dialog binding - jhm 2022/09/14
    private lateinit var couponNoticeDialogBinding: CouponNoticeDialogBinding

    // 생체인증 등록 - jhm 2022/09/15
    private lateinit var certificationDialogBinding: CertificationDialogBinding

    // 생체인증 해제 - jhm 2022/09/16
    private lateinit var certificationOffDialogBinding: CertificationOffDialogBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        when (division) {
            "coupon" -> {
                LogUtil.logE("Coupon Dialog 호출")
                couponDialogBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.coupon_dialog,
                    null, false
                )

                setContentView(couponDialogBinding.root)
                // 다이얼로그 배경 제거
                //window!!.setDimAmount(0f)

                // 다이얼로그 사이즈 조절
                context.dialogResize(this, 1f, 0.3f)

                couponDialogBinding.apply {
                    // 버튼 클릭 시 리스너 메소드 호출
                    confirmBtn.setOnClickListener {
                        listener_c.onOkButtonClicked()
                    }
                    cancleBtn.setOnClickListener {
                        listener_c.onCancelButtonClicked()
                    }
                }
            }

            "coupon_response" -> {
                LogUtil.logE("쿠폰 사용 완료시 Dialog 호출")
                couponResponseDialogBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.coupon_response_dialog,
                    null, false
                )
                setContentView(couponResponseDialogBinding.root)

                LogUtil.logE("message : $message")
                couponResponseDialogBinding.title.text = message.toString()

                // 다이얼로그 사이즈 조절
                context.dialogResize(this, 1f, 0.3f)

                couponResponseDialogBinding.apply {
                    okBtn.setOnClickListener {
                        listener_c.onOkButtonClicked()
                    }
                }
            }

            "coupon_notice" -> {
                LogUtil.logE("쿠폰 유의사항 Dialog 호출")
                couponNoticeDialogBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.coupon_notice_dialog,
                    null, false
                )
                setContentView(couponNoticeDialogBinding.root)
                // 다이얼로그 사이즈 조절
                context.dialogResize(this, 1f, 0.5f)

                couponNoticeDialogBinding.content.text = message.toString()
                couponNoticeDialogBinding.content.text =
                    SpannableStringBuilder(context.getText(R.string.notice)).apply {
                        setSpan(IndentLeadingMarginSpan(), 0, length, 0)
                    }

                couponNoticeDialogBinding.apply {
                    okBtn.setOnClickListener {
                        listener_c.onOkButtonClicked()
                    }
                }
            }

            "certification_on" -> {
                LogUtil.logE("생체인증 등록시 Dialog 호출")
                certificationDialogBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.certification_dialog,
                    null, false
                )
                setContentView(certificationDialogBinding.root)

                // 다이얼로그 사이즈 조절
                context.dialogResize(this, 1f, 0.3f)

                certificationDialogBinding.apply {
                    confirmBtn.setOnClickListener {
                        listener_c.onOkButtonClicked()
                    }
                    cancleBtn.setOnClickListener {
                        listener_c.onCancelButtonClicked()
                    }
                }
            }
            "certification_off" -> {
                LogUtil.logE("생체인증 해제시 Dialog 호출")
                certificationOffDialogBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.certification_off_dialog,
                    null, false
                )
                setContentView(certificationOffDialogBinding.root)

                // 다이얼로그 사이즈 조절
                context.dialogResize(this, 1f, 0.3f)

                certificationOffDialogBinding.apply {
                    confirmBtn.setOnClickListener {
                        // 확인시 생체인증 해제 - jhm 2022/09/16
                        listener_c.onOkButtonClicked()
                    }
                    cancleBtn.setOnClickListener {
                        // 클릭시 생체인증 유지 - jhm 2022/09/16
                        listener_c.onCancelButtonClicked()
                    }
                }
            }


        }

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 취소 가능
        setCancelable(true)
    }
}