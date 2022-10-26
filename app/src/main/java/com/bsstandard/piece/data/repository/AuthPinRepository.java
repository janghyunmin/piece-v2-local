package com.bsstandard.piece.data.repository;


import android.app.Application;

import com.bsstandard.piece.data.datasource.shared.PrefsHelper;
import com.bsstandard.piece.data.dto.AuthPinDTO;
import com.bsstandard.piece.di.hilt.ApiModule;
import com.bsstandard.piece.retrofit.RetrofitService;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.bsstandard.piece.widget.utils.SingleLiveEvent;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * packageName    : com.bsstandard.piece.data.repository
 * fileName       : AuthPinRepository
 * author         : piecejhm
 * date           : 2022/07/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/04        piecejhm       최초 생성
 */
public class AuthPinRepository {
    private static RetrofitService mInstance;
    private static AuthPinRepository authPinRepository;
    private final SingleLiveEvent<AuthPinDTO> authData = new SingleLiveEvent<>();

    public static AuthPinRepository getInstance(Application application) {
        if(authPinRepository == null){
            authPinRepository = new AuthPinRepository(application);
        }
        return authPinRepository;
    }
    public AuthPinRepository(Application application) { mInstance = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);
    }
    public SingleLiveEvent<AuthPinDTO> getAuthData(){
        String memberId = PrefsHelper.read("memberId","");
        String pinNumber = PrefsHelper.read("inputPinNumber","");

        LogUtil.logE("memberId : " + memberId);
        LogUtil.logE("pinNumber : " + pinNumber);

        HashMap<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("pinNumber",pinNumber);

        LogUtil.logE("맴버 아이디 : " + memberId);
        LogUtil.logE("핀번호 : " + pinNumber);
        Call<AuthPinDTO> authPinDTOCall = mInstance.getAuthPin(map);
        authPinDTOCall.enqueue(new Callback<AuthPinDTO>() {
            @Override
            public void onResponse(Call<AuthPinDTO> call, Response<AuthPinDTO> response) {
                if(response.isSuccessful()){
                    LogUtil.logE("핀번호 검증 성공");
                    authData.postValue(response.body());
                }
                else {
                    LogUtil.logE("핀번호 검증 실패.." + response.code());
                    LogUtil.logE("핀번호 검증 실패 메시지 : " + response.message());
                    authData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AuthPinDTO> call, Throwable t) {
                    LogUtil.logE("get /member/auth/pin error..." + t);
                    authData.postValue(null);
            }
        });
        return authData;
    }


}
