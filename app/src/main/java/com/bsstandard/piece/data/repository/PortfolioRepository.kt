package com.bsstandard.piece.data.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.bsstandard.piece.data.dto.portfolio.PortfolioDTO
import com.bsstandard.piece.retrofit.RetrofitClientV2
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : PortfolioRepository
 * author         : piecejhm
 * date           : 2022/07/13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/13        piecejhm       최초 생성
 */
class PortfolioRepository(application: Application){

   val response = RetrofitClientV2.retrofitService.getPortfolio()
   private var portfolioData: MutableLiveData<PortfolioDTO> = MutableLiveData()
   fun getPortfolios(): Observable<PortfolioDTO> = RetrofitClientV2.retrofitService
      .getPortfolio()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())


   // singleton pattern
   companion object {
      private var instance: PortfolioRepository? = null

      fun getInstance(application : Application): PortfolioRepository? {
         if (instance == null) instance = PortfolioRepository(application)
         return instance
      }
   }
}