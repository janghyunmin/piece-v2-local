package com.bsstandard.piece.data.datamodel.dmodel

/**
 *packageName    : com.bsstandard.piece.data.datamodel.dmodel
 * fileName       : Token
 * author         : piecejhm
 * date           : 2022/08/10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/10        piecejhm       최초 생성
 */
data class Token(
    val status: String?,
    val statusCode: String?,
    val message: String?,
    val data:Data?,
)

data class Data (
    val expiredAt: String?,
    val accessToken: String?,
    val refreshToken: String?
)
