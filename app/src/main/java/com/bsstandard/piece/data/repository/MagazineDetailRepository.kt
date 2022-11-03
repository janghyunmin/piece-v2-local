package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.dto.MagazineDetailDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : MagazineDetailRepository
 * author         : piecejhm
 * date           : 2022/08/30
 * description    : 매거진(라운지) 상세 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/30        piecejhm       최초 생성
 */


class MagazineDetailRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)

    suspend fun getNoMemberMagazineDetail(magazineId: String) : MagazineDetailDTO {
        return response.getNoMemberMagazineDetail(magazineId)
    }

    // singleton pattern - jhm 2022/08/30
    companion object {
        private var instance: MagazineDetailRepository?= null

        fun getInstance(application: Application): MagazineDetailRepository? {
            if(instance == null) instance = MagazineDetailRepository(application)
            return instance
        }
    }
}