package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.MemberMagazineDetailDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : MemberMagazineDetailRepository
 * author         : piecejhm
 * date           : 2022/11/01
 * description    : 회원 전용 매거진 상세 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/11/01        piecejhm       최초 생성
 */

class MemberMagazineDetailRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")


    suspend fun getMemberMagazineDetail(magazineId: String): MemberMagazineDetailDTO {
        return response.getMagazineDetail(
            accessToken = "Bearer $accessToken",
            deviceId = deviceId,
            memberId = memberId,
            magazineId = magazineId
        )
    }
    // singleton pattern - jhm 2022/08/30
    companion object {
        private var instance: MemberMagazineDetailRepository?= null

        fun getInstance(application: Application): MemberMagazineDetailRepository? {
            if(instance == null) instance = MemberMagazineDetailRepository(application)
            return instance
        }
    }
}
