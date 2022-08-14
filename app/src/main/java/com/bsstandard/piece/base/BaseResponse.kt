package com.bsstandard.piece.base

/**
 *packageName    : com.bsstandard.piece.base
 * fileName       : BaseResponse
 * author         : piecejhm
 * date           : 2022/08/10
 * description    : Retrofit BaseResponse
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/10        piecejhm       최초 생성
 */


interface BaseResponse<T> {
    fun onSuccess(data: T)

    fun onFail(description: String)

    fun onError(throwable: Throwable)

    fun onLoading()

    fun onLoaded()

}