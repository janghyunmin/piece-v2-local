package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bsstandard.piece.data.dto.CouponDTO
import com.bsstandard.piece.data.repository.CouponRepository
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.widget.utils.LogUtil
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : CouponViewModel
 * author         : piecejhm
 * date           : 2022/09/13
 * description    : 쿠폰 및 프로모션 코드 등록 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/13        piecejhm       최초 생성
 */

class CouponViewModel(application: Application) :
    AndroidViewModel(application) {
    private val repo: CouponRepository = CouponRepository(application)
    val response: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)


    var userCouponCode = MutableLiveData<String>()
    fun getCodeInput(): MutableLiveData<String> {
        return userCouponCode
    }

    // 사용자 입력 값 쿠폰코드 - jhm 2022/09/13
    open fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        LogUtil.logE("tagonTextChanged $s")
    }


    // 쿠폰 사용시 Response Model - jhm 2022/09/14
    private val _couponResponse: MutableLiveData<CouponDTO> = MutableLiveData()
    val couponResponse: LiveData<CouponDTO>
        get() = _couponResponse

    private val _responseCode: MutableLiveData<String> = MutableLiveData()
    val responseCode: LiveData<String> get() = _responseCode

    @SuppressLint("CheckResult")
    fun getCoupon(couponCode: String) {
        // 뷰모델이 사라지면 코루틴도 같이 삭제 - jhm 2022/09/13
        viewModelScope.launch {
            val response = repo.getCouponInput(couponCode = couponCode)
            LogUtil.logE("couponCode $couponCode")
            try {
                response.enqueue(object : Callback<CouponDTO> {
                    override fun onResponse(call: Call<CouponDTO>, response: Response<CouponDTO>) {
                        try {
                            // 쿠폰 성공 - jhm 2022/09/14
                            if (response.code() == 200) {
                                LogUtil.logE("쿠폰번호 정상 입력 !")
                                _responseCode.value = response.code().toString()
                                _responseCode.postValue(response.code().toString())
                            }
                            // 이미 사용한 쿠폰 - jhm 2022/09/14
                            else if (response.code() == 208) {
                                LogUtil.logE("이미 사용된 쿠폰 번호 Error !")
                                _responseCode.value = response.code().toString()
                                _responseCode.postValue(response.code().toString())
                            }
                            // 회원 정보가 일치하지 않을때 - jhm 2022/09/14
                            else if (response.code() == 400) {
                                LogUtil.logE("회원 정보 불일치 Error !")
                                _responseCode.value = response.code().toString()
                                _responseCode.postValue(response.code().toString())
                            }
                            // 사용할 수 있는 쿠폰이 없을때 - jhm 2022/09/14
                            else if (response.code() == 404) {
                                LogUtil.logE("쿠폰번호 잘못 입력 Error !")
                                _responseCode.value = response.code().toString()
                                _responseCode.postValue(response.code().toString())
                            }
                            // 사용 가능한 쿠폰이 아니거나 사용기한을 넘겼을때 - jhm 2022/09/14
                            else {
                                LogUtil.logE("사용 가능한 쿠폰이 아닙니다 !")
                                _responseCode.value = response.code().toString()
                                _responseCode.postValue(response.code().toString())
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                    override fun onFailure(call: Call<CouponDTO>, t: Throwable) {
                        LogUtil.logE("onFail.." + t.message)
                    }
                })
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}