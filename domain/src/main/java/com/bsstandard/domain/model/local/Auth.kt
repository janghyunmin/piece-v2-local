package com.bsstandard.domain.model.local

/**
 *packageName    : com.bsstandard.domain.model.local
 * fileName       : Auth
 * author         : piecejhm
 * date           : 2022/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/03        piecejhm       최초 생성
 */


data class Auth(
    var isDi: String,
    val isWithdrawal: String,
    val isWithdrawalOver: String?
)
