package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.dto.EventDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : EventRepository
 * author         : piecejhm
 * date           : 2022/09/11
 * description    : 이벤트 조회 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/11        piecejhm       최초 생성
 */


class EventRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)

    fun getEvents(): Observable<EventDTO> = response
        .getEvent()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    // singleton pattern - jhm 2022/08/15
    companion object {
        private var instance: EventRepository? = null

        fun getInstance(application : Application): EventRepository? {
            if (instance == null) instance = EventRepository(application)
            return instance
        }
    }
}