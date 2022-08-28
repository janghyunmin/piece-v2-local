package com.bsstandard.piece.data.repository

import android.app.Application
import com.bsstandard.piece.data.dto.PortfolioDetailDTO
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.widget.utils.LogUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : PortfolioDetailRepository
 * author         : piecejhm
 * date           : 2022/08/17
 * description    : 포트폴리오 상세 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/17        piecejhm       최초 생성
 */


class PortfolioDetailRepository(application: Application) {
    val response =  ApiModule.provideRetrofit().create(RetrofitService::class.java)

    suspend fun getPortfolioDetails(portfolioId: String): PortfolioDetailDTO {
        return response.getPortfolioDetail(portfolioId)
    }

//    fun getPortfolioDetails(portfolioId: String) {
//        response.getPortfolioDetail(portfolioId).enqueue(object : Callback<PortfolioDetailDTO> {
//            override fun onResponse(
//                call: Call<PortfolioDetailDTO>,
//                response: Response<PortfolioDetailDTO>
//            ) {
//                if(response.isSuccessful) {
//                    if(response.code() == 200) {
//                        detailResponse.value = response.body()
//
//                        purchaseGuidesList.clear()
//                        for (i in ArrayList(detailResponse.value!!.data.purchaseGuides).indices) {
//                            purchaseGuidesList.add(detailResponse.value!!.data.purchaseGuides[i])
//                        }
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<PortfolioDetailDTO>, t: Throwable) {
//                try {
//                    LogUtil.logE("onFailure : ${t.printStackTrace()}" )
//                }catch (ex : Exception) {
//                    ex.printStackTrace()
//                }
//            }
//        })
//    }

    // singleton pattern - jhm 2022/08/18
    companion object {
        private var instance: PortfolioDetailRepository? = null

        fun getInstance(application: Application): PortfolioDetailRepository? {
            if(instance == null) instance = PortfolioDetailRepository(application)
            return instance
        }
    }
}