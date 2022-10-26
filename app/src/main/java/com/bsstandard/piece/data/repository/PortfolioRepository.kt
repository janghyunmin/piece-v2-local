package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.dto.PortfolioDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : PortfolioRepository
 * author         : piecejhm
 * date           : 2022/07/13
 * description    : 포트폴리오 조회 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/13        piecejhm       최초 생성
 */


class PortfolioRepository(application: Application){
   val response = ApiModule.provideRetrofit().create(RetrofitService::class.java)

   fun getPortfolios(length: Int): Observable<PortfolioDTO> = response
      .getPortfolio(length)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())


   // singleton pattern - jhm 2022/08/15
   companion object {
      private var instance: PortfolioRepository? = null

      fun getInstance(application : Application): PortfolioRepository? {
         if (instance == null) instance = PortfolioRepository(application)
         return instance
      }
   }
}