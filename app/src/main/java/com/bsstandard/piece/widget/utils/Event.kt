package com.bsstandard.piece.widget.utils

import java.util.concurrent.atomic.AtomicBoolean

/**
 *packageName    : com.bsstandard.piece.widget.utils
 * fileName       : Event
 * author         : piecejhm
 * date           : 2022/08/12
 * description    : Activity -> Fragment 뒤로가기 Event Handler
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/12        piecejhm       최초 생성
 */


open class Event<out T>(
    private val content: T
) {
    val hasBeenHandled = AtomicBoolean(false)

    fun getContentIfNotHandled(handleContent: (T) -> Unit) {
        if (!hasBeenHandled.get()) {
            hasBeenHandled.set(true)
            handleContent(content)
        }
    }

    fun peekContent() = content
}