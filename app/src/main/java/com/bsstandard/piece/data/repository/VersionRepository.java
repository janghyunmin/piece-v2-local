package com.bsstandard.piece.data.repository;

import android.app.Application;

import com.bsstandard.piece.data.dto.VersionDTO;
import com.bsstandard.piece.di.hilt.ApiModule;
import com.bsstandard.piece.retrofit.RetrofitService;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.bsstandard.piece.widget.utils.SingleLiveEvent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * packageName    : com.bsstandard.piece.data.repository
 * fileName       : VersionRepository
 * author         : piecejhm
 * date           : 2022/06/13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/13        piecejhm       최초 생성
 */


public class VersionRepository {
    private static RetrofitService mInterface;
    private static VersionRepository versionRepository;
    private final SingleLiveEvent<VersionDTO> versionData = new SingleLiveEvent<>();


    // Repository Singleton - jhm 2022/06/17
    public static VersionRepository getInstance(Application application){
        if(versionRepository == null){
            versionRepository = new VersionRepository(application);
        }
        return versionRepository;
    }

    public VersionRepository(Application application){
//        mInterface = RetrofitClient.getService();
        mInterface = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);
    }

    public SingleLiveEvent<VersionDTO> getVersionData(String deviceType){
        Call<VersionDTO> versionDataCall = mInterface.getVersion(deviceType);
        versionDataCall.enqueue(new Callback<VersionDTO>() {
            @Override
            public void onResponse(Call<VersionDTO> call, Response<VersionDTO> response) {
                try {
                    if(response.body()!= null){
                        LogUtil.logE("get Version : " + response.message());
                        versionData.postValue(response.body());
                    }
                }catch (Exception e){
                    LogUtil.logE("get Version Error " + response.message());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<VersionDTO> call, Throwable t) {
                LogUtil.logE("get Version Error" + t);
                versionData.postValue(null);
            }
        });
        return versionData;
    }
}
