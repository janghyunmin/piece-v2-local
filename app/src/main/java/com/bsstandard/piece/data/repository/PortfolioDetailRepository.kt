package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.dto.PortfolioDetailDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService


/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : PortfolioDetailRepository
 * author         : piecejhm
 * date           : 2022/08/17
 * description    : 포트폴리오 상세 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/17        piecejhm       최초 생성
 */


class PortfolioDetailRepository(application: Application) {
    val response =  ApiModule.provideRetrofit().create(RetrofitService::class.java)

    suspend fun getPortfolioDetails(portfolioId: String): PortfolioDetailDTO {
        return response.getPortfolioDetail(portfolioId)
    }

    // singleton pattern - jhm 2022/08/18
    companion object {
        private var instance: PortfolioDetailRepository? = null

        fun getInstance(application: Application): PortfolioDetailRepository? {
            if(instance == null) instance = PortfolioDetailRepository(application)
            return instance
        }
    }
}