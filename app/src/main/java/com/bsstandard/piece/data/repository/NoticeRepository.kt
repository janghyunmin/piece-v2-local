package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.dto.BoardDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : NoticeRepository
 * author         : piecejhm
 * date           : 2022/09/10
 * description    : 공지사항 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/10        piecejhm       최초 생성
 */
class NoticeRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)

    fun getNoticeList(): Observable<BoardDTO> =
        response.getNotice("BRT02")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    // singleton pattern - jhm 2022/08/15
    companion object {
        private var instance: NoticeRepository? = null

        fun getInstance(application : Application): NoticeRepository? {
            if (instance == null) instance = NoticeRepository(application)
            return instance
        }
    }
}