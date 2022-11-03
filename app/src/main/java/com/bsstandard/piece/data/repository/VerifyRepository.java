package com.bsstandard.piece.data.repository;

import android.app.Application;

import com.bsstandard.piece.data.datamodel.dmodel.sms.CallSmsVerification;
import com.bsstandard.piece.data.dto.SmsVerificationDTO;
import com.bsstandard.piece.di.hilt.ApiModule;
import com.bsstandard.piece.retrofit.RetrofitService;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.bsstandard.piece.widget.utils.SingleLiveEvent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * packageName    : com.bsstandard.piece.data.repository
 * fileName       : VerifyRepository
 * author         : piecejhm
 * date           : 2022/06/24
 * description    : 인증번호 검증 repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/24        piecejhm       최초 생성
 */
public class VerifyRepository {
    private static RetrofitService mInstance;
    private static VerifyRepository verifyRepository;
    private final SingleLiveEvent<SmsVerificationDTO> verifyData = new SingleLiveEvent<>();

    public static VerifyRepository getInstance(Application application) {
        if(verifyRepository == null) {
            verifyRepository = new VerifyRepository(application);
        }
        return verifyRepository;
    }
    public VerifyRepository(Application application) {
        mInstance = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);
    }

    // 한번씩만 실행하게 SingleLiveEvent 사용 - jhm 2022/06/24
    public SingleLiveEvent<SmsVerificationDTO> getVerifyData(CallSmsVerification callSmsVerification){
        Call<SmsVerificationDTO> smsVerificationDTOCall = mInstance.PostVerification(callSmsVerification);
        smsVerificationDTOCall.enqueue(new Callback<SmsVerificationDTO>() {
            @Override
            public void onResponse(Call<SmsVerificationDTO> call, Response<SmsVerificationDTO> response) {
                if(response.body().getData()!=null){
                    LogUtil.logE("인증번호 검증 실행..");
                    verifyData.postValue(response.body());
                } else {
                    LogUtil.logE("post /member/call_sms_verification error " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SmsVerificationDTO> call, Throwable t) {
                LogUtil.logE("post /member/call_sms_verification error..." + t);
                verifyData.postValue(null);
            }
        });
        return verifyData;
    }
}
