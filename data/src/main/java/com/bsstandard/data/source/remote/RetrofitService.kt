package com.bsstandard.data.source.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

/**
 *packageName    : com.bsstandard.data.remote.api
 * fileName       : RetrofitService
 * author         : piecejhm
 * date           : 2022/05/02
 * description    : RetrofitService
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/02        piecejhm       최초 생성
 */


interface RetrofitService {
    @FormUrlEncoded
    @POST("/member/is_di")
    suspend fun getIsDi(
        @Header("Content-Type") content_type : String?,
        @Body DiVerificationRequestModel : Object?
    ): Call<List<AuthRepository>>

}