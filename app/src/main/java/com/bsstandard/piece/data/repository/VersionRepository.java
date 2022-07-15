package com.bsstandard.piece.data.repository;

import com.bsstandard.piece.data.dto.VersionDTO;
import com.bsstandard.piece.retrofit.RetrofitClient;
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
    public static VersionRepository getInstance(){
        if(versionRepository == null){
            versionRepository = new VersionRepository();

        }
        return versionRepository;
    }

    public VersionRepository(){
        mInterface = RetrofitClient.getService();
    }

    public SingleLiveEvent<VersionDTO> getVersionData(String deviceType){
        Call<VersionDTO> versionDataCall = mInterface.getVersion(deviceType);
        versionDataCall.enqueue(new Callback<VersionDTO>() {
            @Override
            public void onResponse(Call<VersionDTO> call, Response<VersionDTO> response) {
                if(response.body()!= null){
                    LogUtil.logE("get Version : " + response.message());
                    versionData.setValue(response.body());
                }else {
                    LogUtil.logE("get Version Error " + response.message());
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
