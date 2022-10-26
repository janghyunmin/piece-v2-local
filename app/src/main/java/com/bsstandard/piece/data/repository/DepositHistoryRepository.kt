package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.DepositHistoryDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : DepositHistoryRepository
 * author         : piecejhm
 * date           : 2022/09/28
 * description    : 회원 거래 내역 목록 조회 요청 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/28        piecejhm       최초 생성
 */


class DepositHistoryRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")
    var changeReason: String = ""

//    suspend fun getDepositHistory(accessToken: String, deviceId: String, memberId: String , changeReason: String) : DepositHistoryDTO {
//        return response.getDepositHistory(accessToken = accessToken, deviceId = deviceId, memberId = memberId, changeReason = changeReason)
//    }

    fun getDepositHistory(
        accessToken: String,
        deviceId: String,
        memberId: String,
        changeReason: String,
        length: Int
    ): Observable<DepositHistoryDTO> = response
        .getDepositHistory("Bearer $accessToken", deviceId, memberId, changeReason , 100)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    companion object {
        private var instance: DepositHistoryRepository? = null
        fun getInstance(application: Application): DepositHistoryRepository? {
            if (instance == null) instance = DepositHistoryRepository(application)
            return instance
        }
    }
}