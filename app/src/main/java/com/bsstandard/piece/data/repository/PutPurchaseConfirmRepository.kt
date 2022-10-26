package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.datamodel.dmodel.purchase.RequestPurchaseConfirmModel
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : PutPurchaseConfirmRepository
 * author         : piecejhm
 * date           : 2022/10/25
 * description    : 구매 확정 요청 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/25        piecejhm       최초 생성
 */


class PutPurchaseConfirmRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken","")
    val deviceId: String = PrefsHelper.read("deviceId","")
    val memberId:String = PrefsHelper.read("memberId","")
    val requestPurchaseConfirmModel : RequestPurchaseConfirmModel? = null

    fun putPurchaseConfirm(accessToken: String, deviceId: String, memberId: String, requestPurchaseConfirmModel: RequestPurchaseConfirmModel) = ApiModule.provideRetrofit().create(RetrofitService::class.java)
        .putPurchaseConfirm("Bearer $accessToken",deviceId,memberId,requestPurchaseConfirmModel)

    // singleton pattern - jhm 2022/09/13
    companion object {
        private var instance: PutPurchaseConfirmRepository?= null

        fun getInstance(application: Application): PutPurchaseConfirmRepository? {
            if(instance == null) instance = PutPurchaseConfirmRepository(application)
            return instance
        }
    }
}