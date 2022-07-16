package com.bsstandard.domain.utils

/**
 *packageName    : com.bsstandard.domain.utils
 * fileName       : RemoteErrorEmitter
 * author         : piecejhm
 * date           : 2022/07/11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/11        piecejhm       최초 생성
 */

interface RemoteErrorEmitter {
    fun onError(msg: String)
    fun onError(errorType: ErrorType)
}