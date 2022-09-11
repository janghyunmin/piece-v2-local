package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.dto.BoardDetailDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : NoticeDetailRepository
 * author         : piecejhm
 * date           : 2022/09/11
 * description    : 공지사항 Detail Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/11        piecejhm       최초 생성
 */

class NoticeDetailRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)

    suspend fun getNoticeDetails(boardId: String) : BoardDetailDTO {
        return response.getNoticeDetail(boardId = boardId)
    }

    // singleton pattern - jhm 2022/08/30
    companion object {
        private var instance: NoticeDetailRepository?= null

        fun getInstance(application: Application): NoticeDetailRepository? {
            if(instance == null) instance = NoticeDetailRepository(application)
            return instance
        }
    }
}