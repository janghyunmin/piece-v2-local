package com.bsstandard.piece.data.repository;

import android.app.Application;

import com.bsstandard.piece.data.datamodel.dmodel.occupation.OccupationVerifyModel;
import com.bsstandard.piece.data.dto.OccupationDTO;
import com.bsstandard.piece.di.hilt.ApiModule;
import com.bsstandard.piece.retrofit.RetrofitService;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.bsstandard.piece.widget.utils.SingleLiveEvent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * packageName    : com.bsstandard.piece.data.repository
 * fileName       : OccupationRepository
 * author         : piecejhm
 * date           : 2022/10/06
 * description    : 가상계좌 휴대폰 점유인증 인증 문자 검증
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/06        piecejhm       최초 생성
 */


public class OccupationRepository {
    private static RetrofitService mInstance;
    private static OccupationRepository occupationRepository;
    private final SingleLiveEvent<OccupationDTO> data = new SingleLiveEvent<>();

    public static OccupationRepository getInstance(Application application) {
        if(occupationRepository == null) {
            occupationRepository = new OccupationRepository(application);
        }
        return occupationRepository;
    }

    public OccupationRepository(Application application) {
        mInstance = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);
    }

    public SingleLiveEvent<OccupationDTO> getVerifyData(String accessToken, String deviceId, String memberId, OccupationVerifyModel occupationVerifyModel) {
        Call<OccupationDTO> verifyData = mInstance.postOccupationVerify(accessToken,deviceId,memberId,occupationVerifyModel);
        verifyData.enqueue(new Callback<OccupationDTO>() {
            @Override
            public void onResponse(Call<OccupationDTO> call, Response<OccupationDTO> response) {
                if(response.isSuccessful()) {
                    if(response.body().getData()!= null) {
                        data.postValue(response.body());
                        LogUtil.logE("deposit/occupation/verify Success ! " + response.message());
                    } else {
                        data.postValue(response.body());
                        LogUtil.logE("deposit/occupation/verify Error ! " + response.message());
                    }
                } else {
                    data.postValue(response.body());
                    LogUtil.logE("deposit/occupation/verify Error ! " + response.message());
                }
            }

            @Override
            public void onFailure(Call<OccupationDTO> call, Throwable t) {
                t.printStackTrace();
                LogUtil.logE("deposit/occupation/verify Error ! " + t.getMessage());
                data.postValue(null);
            }
        });
        return data;
    }
}
