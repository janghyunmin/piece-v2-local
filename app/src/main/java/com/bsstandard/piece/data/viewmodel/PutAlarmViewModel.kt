package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bsstandard.piece.data.dto.BaseDTO
import com.bsstandard.piece.data.repository.PutAlarmRepository
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.widget.utils.LogUtil
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : PutAlarmViewModel
 * author         : piecejhm
 * date           : 2022/10/17
 * description    : 사용자 알림 읽음 처리 요청 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/17        piecejhm       최초 생성
 */


class PutAlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: PutAlarmRepository = PutAlarmRepository(application)
    val response: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)


    private val _putAlarmResponse: MutableLiveData<BaseDTO> = MutableLiveData()
    val putAlarmResponse: LiveData<BaseDTO>
        get() = _putAlarmResponse

    @SuppressLint("CheckResult")
    fun putAlaram() {
        // 뷰모델이 사라지면 코루틴도 같이 삭제 - jhm 2022/09/13
        viewModelScope.launch {
            val response = repo.putAlarm()
            try {
                LogUtil.logE("사용자 알림 읽음 처리 요청 Call..")
                response.enqueue(object : Callback<BaseDTO> {
                    override fun onResponse(call: Call<BaseDTO>, response: Response<BaseDTO>) {
                        try {
                            if (response.isSuccessful) {
                                if (response.code() == 200) {
                                    LogUtil.logE("사용자 알림 읽음 처리 요청 Call Success..")
                                    _putAlarmResponse.value = response.body()
                                }
                            } else {
                                LogUtil.logE("member/alarm isFail..")
                                _putAlarmResponse.value = response.body()
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                    override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                        t.printStackTrace()
                        LogUtil.logE("member/alarm isFail.." + t.message)
                    }
                })
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }

}