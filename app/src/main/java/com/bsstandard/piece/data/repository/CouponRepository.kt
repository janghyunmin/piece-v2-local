package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : CouponRepository
 * author         : piecejhm
 * date           : 2022/09/13
 * description    : 쿠폰 등록 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/13        piecejhm       최초 생성
 */

class CouponRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken","")
    val deviceId: String = PrefsHelper.read("deviceId","")
    val memberId:String = PrefsHelper.read("memberId","")


//    suspend fun getCouponInput(couponCode: String) : CouponDTO {
//        LogUtil.logE("couponCode : $couponCode")
//        return response.getCouponCodeInput(couponCode = couponCode , "Bearer $accessToken",deviceId,memberId)
//    }

    fun getCouponInput(couponCode: String) = ApiModule.provideRetrofit().create(RetrofitService::class.java).getCouponCodeInput(
        couponCode, "Bearer $accessToken", deviceId, memberId
    )

    // singleton pattern - jhm 2022/09/13
    companion object {
        private var instance: CouponRepository?= null

        fun getInstance(application: Application): CouponRepository? {
            if(instance == null) instance = CouponRepository(application)
            return instance
        }
    }
}