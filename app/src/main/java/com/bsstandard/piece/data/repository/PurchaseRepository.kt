package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.PurchaseDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : PurchaseRepository
 * author         : piecejhm
 * date           : 2022/10/09
 * description    : 내지갑 - 소유조각 목록 조회 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/09        piecejhm       최초 생성
 */


class PurchaseRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")



    fun getPurchaseList(
        accessToken: String,
        deviceId: String,
        memberId: String
    ): Observable<PurchaseDTO> = response
        .getPurchase(
            accessToken = accessToken,
        deviceId = deviceId,
        memberId = memberId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    companion object {
        private var instance: PurchaseRepository? = null
        fun getInstance(application: Application): PurchaseRepository? {
            if (instance == null) instance = PurchaseRepository(application)
            return instance
        }
    }

}