package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.PortfolioNotiDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : PortfolioNotificationRepository
 * author         : piecejhm
 * date           : 2022/11/02
 * description    : 포트폴리오 알림 설정 여부 조회 요청 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/11/02        piecejhm       최초 생성
 */


class PortfolioNotificationRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken","")
    val deviceId: String = PrefsHelper.read("deviceId","")
    val memberId:String = PrefsHelper.read("memberId","")


    // 포트폴리오 알림 설정 여부 조회 - jhm 2022/11/02
    suspend fun getPortfolioNotification(portfolioId: String): PortfolioNotiDTO {
        return response.getPortfolioNotification(
            accessToken = "Bearer $accessToken",
            deviceId = deviceId,
            memberId = memberId,
            portfolioId = portfolioId
        )
    }
    // singleton pattern - jhm 2022/08/15
    companion object {
        private var instance: PortfolioNotificationRepository? = null

        fun getInstance(application : Application): PortfolioNotificationRepository? {
            if (instance == null) instance = PortfolioNotificationRepository(application)
            return instance
        }
    }
}