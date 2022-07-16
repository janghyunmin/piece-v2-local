package com.bsstandard.domain.utils

/**
 *packageName    : com.bsstandard.domain.utils
 * fileName       : ErrorType
 * author         : piecejhm
 * date           : 2022/07/11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/11        piecejhm       최초 생성
 */

enum class ErrorType {
    NETWORK,
    TIMEOUT,
    SESSION_EXPIRED,
    UNKNOWN
}