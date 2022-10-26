package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bsstandard.piece.data.datamodel.dmodel.purchase.RequestPurchaseConfirmModel
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.BaseDTO
import com.bsstandard.piece.data.repository.PutPurchaseConfirmRepository
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.widget.utils.LogUtil
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : PurchaseConfirmViewModel
 * author         : piecejhm
 * date           : 2022/10/25
 * description    : 포트폴리오 구매 확정 요청
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/25        piecejhm       최초 생성
 */

class PurchaseConfirmViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: PutPurchaseConfirmRepository = PutPurchaseConfirmRepository(application)
    val response: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)

    val accessToken: String = PrefsHelper.read("accessToken","")
    val deviceId: String = PrefsHelper.read("deviceId","")
    val memberId:String = PrefsHelper.read("memberId","")


    private val _putPurchaseConfirmResponse: MutableLiveData<BaseDTO> = MutableLiveData()
    val putPurchaseConfirmResponse: LiveData<BaseDTO>
    get() = _putPurchaseConfirmResponse
    private val requestPurchaseConfirmModel : RequestPurchaseConfirmModel? = null


    @SuppressLint("CheckResult")
    fun putPurchaseConfirm(purchaseId: String, portfolioId: String , isAgree: String) {
        viewModelScope.launch {
            val requestPurchaseConfirmModel = RequestPurchaseConfirmModel(purchaseId,portfolioId,isAgree)
            val response = repo.putPurchaseConfirm(accessToken,deviceId,memberId,requestPurchaseConfirmModel)
            try {
                LogUtil.logE("포트폴리오 구매 확정 요청 Call..")
                response.enqueue(object : Callback<BaseDTO> {
                    override fun onResponse(call: Call<BaseDTO>, response: Response<BaseDTO>) {
                       try {
                           LogUtil.logE("포트폴리오 구매 확정 요청 Try..")
                           if(response.isSuccessful) {
                               if(response.code() == 200) {
                                   LogUtil.logE("포트폴리오 구매 확정처리 Success")
                                   _putPurchaseConfirmResponse.value = response.body()
                               }
                           } else {
                               LogUtil.logE("포트폴리오 구매 확정 Fail..")
                               _putPurchaseConfirmResponse.value = response.body()
                           }
                       } catch (ex: Exception) {
                           ex.printStackTrace()
                           LogUtil.logE("포트폴리오 구매 확정 Fail..")
                       }
                    }

                    override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                        t.printStackTrace()
                        LogUtil.logE("purchase/confirm isFail.." + t.message)
                    }
                })
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}