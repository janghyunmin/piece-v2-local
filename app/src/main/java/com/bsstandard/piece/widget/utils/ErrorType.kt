package com.bsstandard.piece.widget.utils

/**
 *packageName    : com.bsstandard.piece.utils
 * fileName       : ErrorType
 * author         : piecejhm
 * date           : 2022/04/27
 * description    : Network 에러 타입 정의
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/27        piecejhm       최초 생성
 */


enum class ErrorType {
    NETWORK,
    TIMEOUT,
    SESSION_EXPIRED,
    UNKNOWN
}