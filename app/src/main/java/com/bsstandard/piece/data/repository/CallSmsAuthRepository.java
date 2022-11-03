package com.bsstandard.piece.data.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.data.datamodel.dmodel.sms.CallSmsAuthModel;
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
 * description    : 문자 발송 요청 Repository
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

    public MutableLiveData<CallSmsAuthDTO> getCallSmsAuthData(CallSmsAuthModel callSmsAuthModel){
        Call<CallSmsAuthDTO> callSmsAuthDTOCall = mInstance.PostSmsCall(callSmsAuthModel);
        callSmsAuthDTOCall.enqueue(new Callback<CallSmsAuthDTO>() {
            @Override
            public void onResponse(Call<CallSmsAuthDTO> call, Response<CallSmsAuthDTO> response) {

                try {
                    callSmsAuthData.postValue(response.body());
                } catch (Exception exception) {
                    exception.printStackTrace();
                    LogUtil.logE("/member/call_sms_auth Error !" + exception.getMessage());
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
