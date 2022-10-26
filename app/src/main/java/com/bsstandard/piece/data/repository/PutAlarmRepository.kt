package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : PutAlarmRepository
 * author         : piecejhm
 * date           : 2022/10/17
 * description    : 사용자 알림 읽음 처리 요청
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/17        piecejhm       최초 생성
 */


class PutAlarmRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken","")
    val deviceId: String = PrefsHelper.read("deviceId","")
    val memberId:String = PrefsHelper.read("memberId","")

    fun putAlarm() = ApiModule.provideRetrofit().create(RetrofitService::class.java)
        .putAlarm("Bearer $accessToken" , deviceId , memberId)

    // singleton pattern - jhm 2022/09/13
    companion object {
        private var instance: PutAlarmRepository?= null

        fun getInstance(application: Application): PutAlarmRepository? {
            if(instance == null) instance = PutAlarmRepository(application)
            return instance
        }
    }
}