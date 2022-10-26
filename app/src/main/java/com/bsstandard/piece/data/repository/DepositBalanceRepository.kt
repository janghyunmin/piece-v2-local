package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.DepositDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : DepositBalanceRepository
 * author         : piecejhm
 * date           : 2022/09/20
 * description    : 회원 예치금 잔액 조회 요청 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/20        piecejhm       최초 생성
 */


class DepositBalanceRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken","")
    val deviceId: String = PrefsHelper.read("deviceId","")
    val memberId:String = PrefsHelper.read("memberId","")


    suspend fun getDepositBalance(accessToken: String, deviceId: String, memberId: String) : DepositDTO {
        return response.getDepositBalance(accessToken = accessToken, deviceId = deviceId, memberId = memberId)
    }

    companion object {
        private var instance: DepositBalanceRepository? = null
        fun getInstance(application: Application) : DepositBalanceRepository? {
            if(instance == null) instance = DepositBalanceRepository(application)
            return instance
        }
    }
}