package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.AlarmDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : AlarmRepository
 * author         : piecejhm
 * date           : 2022/10/16
 * description    : 유저 알림 설정 목록 조회 요청 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/16        piecejhm       최초 생성
 */


class AlarmRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")
    var notificationType: String = ""

    fun getAlarm(
        accessToken: String,
        deviceId: String,
        memberId: String,
        length: Int,
        notificationType: String,
    ): Observable<AlarmDTO> = response
        .getAlarm(accessToken, deviceId, memberId, length, notificationType)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    companion object {
        private var instance: AlarmRepository? = null
        fun getInstance(application: Application): AlarmRepository? {
            if (instance == null) instance = AlarmRepository(application)
            return instance
        }
    }
}