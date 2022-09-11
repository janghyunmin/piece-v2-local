package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.dto.EventDetailDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : EventDetailRepository
 * author         : piecejhm
 * date           : 2022/09/11
 * description    : 이벤트 Detail Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/11        piecejhm       최초 생성
 */

class EventDetailRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)

    suspend fun getEventDetails(eventId: String) : EventDetailDTO {
        return response.getEventDetail(eventId = eventId)
    }

    // singleton pattern - jhm 2022/08/30
    companion object {
        private var instance: EventDetailRepository?= null

        fun getInstance(application: Application): EventDetailRepository? {
            if(instance == null) instance = EventDetailRepository(application)
            return instance
        }
    }

}