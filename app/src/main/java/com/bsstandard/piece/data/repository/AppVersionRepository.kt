package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.VersionDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : AppVersionRepository
 * author         : piecejhm
 * date           : 2022/11/03
 * description    : 앱 버전 조회 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/11/03        piecejhm       최초 생성
 */


class AppVersionRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val memberId:String = PrefsHelper.read("memberId","")

    // 앱 버전 조회 - jhm 2022/11/03
    suspend fun getAppVersion(deviceType: String): VersionDTO {
        return response.getVersion(
            memberId = memberId,
            deviceType = deviceType
        )
    }


    // singleton pattern - jhm 2022/08/15
    companion object {
        private var instance: AppVersionRepository? = null

        fun getInstance(application : Application): AppVersionRepository? {
            if (instance == null) instance = AppVersionRepository(application)
            return instance
        }
    }

}