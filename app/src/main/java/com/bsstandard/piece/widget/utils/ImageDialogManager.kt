package com.bsstandard.piece.widget.utils

import android.app.Dialog
import android.content.Context
import com.bsstandard.piece.R
import com.bsstandard.piece.databinding.DocumentImageDialogBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

/**
 *packageName    : com.bsstandard.piece.widget.utils
 * fileName       : ImageDialogManager
 * author         : piecejhm
 * date           : 2022/10/13
 * description    : 구성품 이미지 클릭시 Dialog Setting
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/13        piecejhm       최초 생성
 */


object ImageDialogManager {
    fun getDialog(
        context: Context,
        name: String,
        target: ImageCloseListener
    ) {
        val dialog = Dialog(context, R.style.Dialog)
        val dialogBinding = DocumentImageDialogBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(10))

        Glide.with(context)
            .load(name)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(dialogBinding.documentImagePath)


        dialogBinding.closeBtn.setOnClickListener {
            target.onClickCancelButton()
            dialog.dismiss()
        }

        dialog.show()
    }
}