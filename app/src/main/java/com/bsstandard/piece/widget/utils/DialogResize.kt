package com.bsstandard.piece.widget.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager

/**
 *packageName    : com.bsstandard.piece.widget.utils
 * fileName       : DialogResize
 * author         : piecejhm
 * date           : 2022/07/04
 * description    : CustomDialog Resize
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/04        piecejhm       최초 생성
 */


fun Context.dialogResize(dialog: Dialog, width: Float, height: Float){
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    if (Build.VERSION.SDK_INT < 30){
        val display = windowManager.defaultDisplay
        val size = Point()

        display.getSize(size)

        val window = dialog.window

        val x = (size.x * width).toInt()
        val y = (size.y * height).toInt()

        window?.setLayout(x, y)

    }else{
        val rect = windowManager.currentWindowMetrics.bounds

        val window = dialog.window
        val x = (rect.width() * width).toInt()
        val y = (rect.height() * height).toInt()

        window?.setLayout(x, y)
    }
}