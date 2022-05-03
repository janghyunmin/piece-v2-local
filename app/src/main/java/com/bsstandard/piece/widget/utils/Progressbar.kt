package com.bsstandard.piece.widget.utils

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

/**
 *packageName    : com.bsstandard.piece.utils
 * fileName       : Progressbar
 * author         : piecejhm
 * date           : 2022/04/27
 * description    : visibility Common Module
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/27        piecejhm       최초 생성
 */


@BindingAdapter("isVisible")
fun ProgressBar.bindVisible(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}