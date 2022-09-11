package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.MagazineDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : MagazineRepository
 * author         : piecejhm
 * date           : 2022/08/25
 * description    : 라운지 조회
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/25        piecejhm       최초 생성
 */

class MagazineRepository(application: Application) {
    val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)
    val accessToken: String = PrefsHelper.read("accessToken","")
    val deviceId: String = PrefsHelper.read("deviceId","")
    val memberId:String = PrefsHelper.read("memberId","")


    // 비로그인 전용 라운지 조회  - jhm 2022/08/30
    fun getNoMemberMagazine(magazineType:String): Observable<MagazineDTO> = response
        .getNoMemberMagazine(magazineType)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())


    // 라운지 전체 - jhm 2022/08/28
    fun getMagazine(magazineType: String): Observable<MagazineDTO> = response
        .getMagazine("Bearer $accessToken",deviceId,memberId , magazineType)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    // 라운지 - 포트폴리오 - jhm 2022/08/28
    fun getMagazinePortfolio() : Observable<MagazineDTO> = response
        .getMagazine("Bearer $accessToken",deviceId,memberId,"MZT0201")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    // 라운지 - 핀테크 - jhm 2022/08/28
    fun getMagazineFintech() : Observable<MagazineDTO> = response
        .getMagazine("Bearer $accessToken",deviceId,memberId,"MZT0101")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    // 라운지 - 핫플레이스 - jhm 2022/08/28
    fun getMagazinePlace() : Observable<MagazineDTO> = response
        .getMagazine("Bearer $accessToken",deviceId,memberId,"MZT0102")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    // 라운지 - 쿨피플 - jhm 2022/08/28
    fun getMagazinePeople() : Observable<MagazineDTO> = response
        .getMagazine("Bearer $accessToken",deviceId,memberId,"MZT0103")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    // 라운지 - 잘알못 - jhm 2022/08/28
    fun getMagazineJal() : Observable<MagazineDTO> = response
        .getMagazine("Bearer $accessToken",deviceId,memberId,"MZT0104")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())


    // singleton pattern - jhm 2022/08/15
    companion object {
        private var instance: MagazineRepository? = null

        fun getInstance(application : Application): MagazineRepository? {
            if (instance == null) instance = MagazineRepository(application)
            return instance
        }
    }
}