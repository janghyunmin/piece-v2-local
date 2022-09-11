package com.bsstandard.piece.widget.utils

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


open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}