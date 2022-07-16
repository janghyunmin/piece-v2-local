package com.bsstandard.data.api

import com.bsstandard.data.model.PortfolioInfo
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

/**
 *packageName    : com.bsstandard.data.api
 * fileName       : ApiInterface
 * author         : piecejhm
 * date           : 2022/07/12
 * description    : 서버와 통신할 API 리스트
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/12        piecejhm       최초 생성
 */

interface ApiInterface {
    @GET("portfolio")
    suspend fun getPortfolio() : Response<PortfolioInfo.PortfolioResponse>

}