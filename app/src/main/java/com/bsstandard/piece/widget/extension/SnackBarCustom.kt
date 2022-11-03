package com.bsstandard.piece.widget.extension

import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bsstandard.piece.R
import com.bsstandard.piece.databinding.CustomSnackbarBinding
import com.google.android.material.snackbar.Snackbar

/**
 *packageName    : com.bsstandard.piece.widget.extension
 * fileName       : SnackBarCustom
 * author         : piecejhm
 * date           : 2022/11/01
 * description    : SnackBar Custom
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/11/01        piecejhm       최초 생성
 */


class SnackBarCustom(view: View, private val message: String, private val drawable: Drawable) {

    companion object {
        fun make(view: View, message: String, drawable: Drawable) =
            SnackBarCustom(view, message, drawable)
    }

    private val context = view.context
    private val snackBar = Snackbar.make(view, "", 2000)
    private val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val snackBarBinding: CustomSnackbarBinding =
        DataBindingUtil.inflate(inflater, R.layout.custom_snackbar, null, false)

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackBarLayout) {
            val layoutParams = layoutParams as FrameLayout.LayoutParams

            //애니메이션 추가
            //정석은 snackBar을 extend한 class 에서 contentViewCallback 을 커스텀 해주는거 같음
            //그러나 나는 야매로 snackbarLayout에 animation을 줬다.
            val snackBarShowAnim = AnimationUtils.loadAnimation(context, R.anim.snackbar_show_anim)
            val snackBarHideAnim = AnimationUtils.loadAnimation(context, R.anim.snackbar_hide_anim)
            this.startAnimation(snackBarShowAnim) // 👈 시작할때 애니메이션

            Handler(Looper.getMainLooper()).postDelayed({
                this.startAnimation(snackBarHideAnim)
            }, 2000L) // 👈 핸들러로 메인 쓰레드 2초 잠재우고, 스낵바 hide 하는 애니메이션 실행

            layoutParams.gravity = Gravity.BOTTOM // 👈 gravity 설정
            removeAllViews()
            //setPadding(0, 0, 0, 16) // 👈 padding 설정. 위에서 16만큼 떨어져있게.
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackBarBinding.root, 0)
        }

    }

    private fun initData() {
        snackBarBinding.tvSample.text = message
        snackBarBinding.image.background = drawable
    }

    fun show() {
        snackBar.show()
    }
}