package com.bsstandard.piece.widget.utils

import android.graphics.Rect
import android.view.ViewTreeObserver
import android.view.Window

/**
 *packageName    : com.bsstandard.piece.widget.utils
 * fileName       : KeyboardVisibilityUtils
 * author         : piecejhm
 * date           : 2022/06/15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/15        piecejhm       최초 생성
 */

class KeyboardVisibilityUtils(
    private val window: Window,
    private val onShowKeyboard: ((keyboardHeight: Int) -> Unit)? = null,
    private val onHideKeyboard: (() -> Unit)? = null
) {

    private val MIN_KEYBOARD_HEIGHT_PX = 150

    private val windowVisibleDisplayFrame = Rect()
    private var lastVisibleDecorViewHeight: Int = 0


    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        window.decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
        val visibleDecorViewHeight = windowVisibleDisplayFrame.height()

        // Decide whether keyboard is visible from changing decor view height.
        if (lastVisibleDecorViewHeight != 0) {
            if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                // Calculate current keyboard height (this includes also navigation bar height when in fullscreen mode).
                val currentKeyboardHeight = window.decorView.height - windowVisibleDisplayFrame.bottom
                // Notify listener about keyboard being shown.
                onShowKeyboard?.invoke(currentKeyboardHeight)
            } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                // Notify listener about keyboard being hidden.
                onHideKeyboard?.invoke()
            }
        }
        // Save current decor view height for the next call.
        lastVisibleDecorViewHeight = visibleDecorViewHeight
    }

    init {
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    fun detachKeyboardListeners() {
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }
}