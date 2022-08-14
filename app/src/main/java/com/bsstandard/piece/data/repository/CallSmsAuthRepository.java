package com.bsstandard.piece.data.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.data.datamodel.dmodel.SmsAuthModel;
import com.bsstandard.piece.data.dto.CallSmsAuthDTO;
import com.bsstandard.piece.di.hilt.ApiModule;
import com.bsstandard.piece.retrofit.RetrofitService;
import com.bsstandard.piece.widget.utils.LogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * packageName    : com.bsstandard.piece.data.repository
 * fileName       : CallSmsAuthRepository
 * author         : piecejhm
 * date           : 2022/06/22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/22        piecejhm       최초 생성
 */
public class CallSmsAuthRepository {
    private static RetrofitService mInstance;
    private static CallSmsAuthRepository callSmsAuthRepository;
    private final MutableLiveData<CallSmsAuthDTO> callSmsAuthData = new MutableLiveData<>();


    public static CallSmsAuthRepository getInstance(Application application){
        if(callSmsAuthRepository == null){
            callSmsAuthRepository = new CallSmsAuthRepository(application);
        }
        return callSmsAuthRepository;
    }

    public CallSmsAuthRepository(Application application) {
//      mInstance = RetrofitClient.getService();
        mInstance = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);
    }

    public MutableLiveData<CallSmsAuthDTO> getCallSmsAuthData(SmsAuthModel smsAuthModel){
        Call<CallSmsAuthDTO> callSmsAuthDTOCall = mInstance.reqSmsAuth(smsAuthModel);
        callSmsAuthDTOCall.enqueue(new Callback<CallSmsAuthDTO>() {
            @Override
            public void onResponse(Call<CallSmsAuthDTO> call, Response<CallSmsAuthDTO> response) {
                LogUtil.logE("response : " + response);
                LogUtil.logE("response getStatus : " + response.body().getStatus());
                LogUtil.logE("response getMessage : " + response.body().getMessage());
                LogUtil.logE("response getData : " + response.body().getData().getRsltMsg());
                if (response.body().getData()!=null){
                    callSmsAuthData.postValue(response.body());
                } else {
                    LogUtil.logE("post /member/call_sms_auth error.. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CallSmsAuthDTO> call, Throwable t) {
                LogUtil.logE("post /member/call_sms_auth error..." + t);
                callSmsAuthData.postValue(null);
            }
        });
        return callSmsAuthData;
    }

    public MutableLiveData<CallSmsAuthDTO> getSmsAuthData() {
        return callSmsAuthData;
    }
}
