package com.bsstandard.piece.data.repository;

import android.app.Application;

import com.bsstandard.piece.data.datamodel.dmodel.MemberPinModel;
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
 * fileName       : AuthPinPutRepository
 * author         : piecejhm
 * date           : 2022/07/05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/05        piecejhm       최초 생성
 */
public class AuthPinPutRepository {
    private static RetrofitService mInstance;
    private static AuthPinPutRepository authPinPutRepository;
    private final SingleLiveEvent<AuthPinDTO> authPutData = new SingleLiveEvent<>();

    public static AuthPinPutRepository getInstance(Application application) {
        if(authPinPutRepository == null){
            authPinPutRepository = new AuthPinPutRepository(application);
        }
        return authPinPutRepository;
    }
//    public AuthPinPutRepository() { mInstance = RetrofitClient.getService(); }
    public AuthPinPutRepository(Application application) { mInstance = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);
    }
    public SingleLiveEvent<AuthPinDTO> putAuthData(MemberPinModel memberPinModel) {
        String accessToken = PrefsHelper.read("accessToken","");
        String deviceId = PrefsHelper.read("deviceId","");
        String memberId = PrefsHelper.read("memberId","");

        HashMap<String,String> map = new HashMap<>();
        map.put("accessToken",accessToken);
        map.put("deviceId",deviceId);
        map.put("memberId",memberId);

        LogUtil.logE("토큰 : " + accessToken);
        LogUtil.logE("deviceId : " + deviceId);
        LogUtil.logE("memberId : " + memberId);

        Call<AuthPinDTO> putAuthPinCall = mInstance.putAuthPin(map,memberPinModel);
        putAuthPinCall.enqueue(new Callback<AuthPinDTO>() {
            @Override
            public void onResponse(Call<AuthPinDTO> call, Response<AuthPinDTO> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus().equals("OK")){
                        LogUtil.logE("회원 간편비밀번호 변경 성공..");
                        authPutData.postValue(response.body());
                    } else {
                        LogUtil.logE("회원 간편비밀번호 변경 실패..");
                        authPutData.postValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthPinDTO> call, Throwable t) {
                    LogUtil.logE("put /member/auth/pin error..." + t);
                    authPutData.postValue(null);
            }
        });
        return authPutData;
    }
}
