package com.bsstandard.piece.widget.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 *packageName    : com.bsstandard.piece.utils
 * fileName       : ImageView
 * author         : piecejhm
 * date           : 2022/04/27
 * description    : ImageView Glide 모듈
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/27        piecejhm       최초 생성
 */

@BindingAdapter(
    value = [
        "imgSrc",
        "imgPlaceholder",
        "imgError"
    ], requireAll = false
)

fun ImageView.bindImagePlaceHolder(src: String?, placeholder: Drawable?, error: Drawable?) {
    src ?: return

    Glide.with(this)
        .load(src)
        .error(error)
        .placeholder(placeholder)
        .into(this)
}

