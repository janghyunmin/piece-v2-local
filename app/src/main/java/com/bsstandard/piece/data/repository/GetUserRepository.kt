package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.MemberDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : GetMemberUpdateRepository
 * author         : piecejhm
 * date           : 2022/09/06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/06        piecejhm       최초 생성
 */
class GetUserRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    fun getUser(): Observable<MemberDTO?>? = response
        .getMember("Bearer $accessToken", deviceId, memberId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    // singleton pattern - jhm 2022/08/15
    companion object {
        private var instance: GetUserRepository? = null

        fun getInstance(application : Application): GetUserRepository? {
            if (instance == null) instance = GetUserRepository(application)
            return instance
        }
    }

}