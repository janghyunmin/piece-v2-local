package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.dto.PopupDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : PopupRepository
 * author         : piecejhm
 * date           : 2022/10/29
 * description    : 팝업 조회 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/29        piecejhm       최초 생성
 */

class PopupRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)

    fun getPopup(popupType:String) : Observable<PopupDTO> = response
        .getPopup(popupType = popupType)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    // singleton pattern - jhm 2022/08/15
    companion object {
        private var instance: PopupRepository? = null

        fun getInstance(application : Application): PopupRepository? {
            if (instance == null) instance = PopupRepository(application)
            return instance
        }
    }

}