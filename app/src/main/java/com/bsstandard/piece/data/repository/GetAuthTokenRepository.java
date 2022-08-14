package com.bsstandard.piece.data.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.data.datasource.shared.PrefsHelper;
import com.bsstandard.piece.data.dto.BaseDTO;
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
 * fileName       : TokenRepository
 * author         : piecejhm
 * date           : 2022/08/10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/10        piecejhm       최초 생성
 */
public class GetAuthTokenRepository {
    private static RetrofitService mInstance;
    private static GetAuthTokenRepository getAuthTokenRepository;
    private final MutableLiveData<BaseDTO> tokenData = new MutableLiveData<>();

    private String accessToken  = PrefsHelper.read("accessToken","");
    private String deviceId = PrefsHelper.read("deviceId","");
    private String memberId = PrefsHelper.read("memberId","");


    public static GetAuthTokenRepository getInstance(Application application) {
        if(getAuthTokenRepository == null) {
            getAuthTokenRepository = new GetAuthTokenRepository(application);
        }
        return getAuthTokenRepository;
    }

    public GetAuthTokenRepository(Application application) {
        mInstance = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);
    }

    public MutableLiveData<BaseDTO> getAuthTokenData() {
        HashMap<String,String> map = new HashMap<>();
        map.put("accessToken","Bearer " + accessToken);
        map.put("deviceId",deviceId);
        map.put("grantType","client_credentials");
        map.put("memberId",memberId);


        Call<BaseDTO> getAuthToken = mInstance.getAccessToken(map);
        getAuthToken.enqueue(new Callback<BaseDTO>() {
            @Override
            public void onResponse(Call<BaseDTO> call, Response<BaseDTO> response) {
                if(response.isSuccessful()) {
                    LogUtil.logE("accessToken 검증 성공" + response.body());
                    tokenData.setValue(response.body());
                } else {
                    LogUtil.logE("accessToken 검증 실패" + response.body());
                    tokenData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseDTO> call, Throwable t) {
                    LogUtil.logE("get /member/auth error.." + t);
                    tokenData.setValue(null);
            }
        });
        return tokenData;
    }

}
