package com.bsstandard.piece.data.repository;

import android.app.Application;

import com.bsstandard.piece.data.datasource.shared.PrefsHelper;
import com.bsstandard.piece.data.dto.PostTokenDTO;
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
 * fileName       : PutAuthTokenRepository
 * author         : piecejhm
 * date           : 2022/08/11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/11        piecejhm       최초 생성
 */
public class PutAuthTokenRepository {
    private static RetrofitService mInstance;
    private static PutAuthTokenRepository putAuthTokenRepository;
    private final SingleLiveEvent<PostTokenDTO> tokenData = new SingleLiveEvent<>();

    private String accessToken  = PrefsHelper.read("accessToken","");
    private String deviceId = PrefsHelper.read("deviceId","");
    private String memberId = PrefsHelper.read("memberId","");
    private String refreshToken = PrefsHelper.read("refreshToken","");

    public static PutAuthTokenRepository getInstance(Application application) {
        if(putAuthTokenRepository == null) {
            putAuthTokenRepository = new PutAuthTokenRepository(application);
        }
        return putAuthTokenRepository;
    }

    public PutAuthTokenRepository(Application application){
        mInstance = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);
    }

    public SingleLiveEvent<PostTokenDTO> putAuthTokenData() {
        HashMap<String,String> map = new HashMap<>();
        map.put("accessToken","Bearer " + accessToken);
        map.put("deviceId",deviceId);
        map.put("grantType","refresh_token");
        map.put("memberId",memberId);
        map.put("refreshToken",refreshToken);

        Call<PostTokenDTO> putAuthToken = mInstance.putRefreshToken(map);
        putAuthToken.enqueue(new Callback<PostTokenDTO>() {
            @Override
            public void onResponse(Call<PostTokenDTO> call, Response<PostTokenDTO> response) {
                if(response.isSuccessful()) {
                    LogUtil.logE("accessToken refresh 성공");
                    tokenData.setValue(response.body());


                    PrefsHelper.removeToken("expiredAt");
                    PrefsHelper.removeToken("accessToken");
                    PrefsHelper.removeToken("refreshToken");

                    PrefsHelper.write("expiredAt",response.body().getData().getExpiredAt());
                    PrefsHelper.write("accessToken",response.body().getData().getAccessToken());
                    PrefsHelper.write("refreshToken",response.body().getData().getRefreshToken());


                } else {
                    LogUtil.logE("accessToken refresh 실패");
                    tokenData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<PostTokenDTO> call, Throwable t) {
                LogUtil.logE("put /member/auth error.." + t);
                tokenData.setValue(null);
            }
        });
        return tokenData;
    }


}
