package com.bsstandard.piece.widget.utils


/**
 *packageName    : com.bsstandard.piece.utils
 * fileName       : RemoteErrorEmitter
 * author         : piecejhm
 * date           : 2022/04/27
 * description    : Remote 에러 정의 규칙
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/27        piecejhm       최초 생성
 */


interface RemoteErrorEmitter {
    fun onError(msg: String)
    fun onError(errorType: ErrorType)
}