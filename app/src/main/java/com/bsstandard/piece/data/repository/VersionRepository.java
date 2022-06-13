package com.bsstandard.piece.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.dao.VersionDAO;
import com.bsstandard.piece.retrofit.RetrofitClient;
import com.bsstandard.piece.retrofit.RetrofitService;
import com.bsstandard.piece.widget.utils.LogUtil;

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
    private final MutableLiveData<VersionDAO> versionData = new MutableLiveData<>();
    private static VersionRepository versionRepository;

    public static VersionRepository getInstance(){
        if(versionRepository == null){
            versionRepository = new VersionRepository();
        }
        return versionRepository;
    }

    public VersionRepository(){
        mInterface = RetrofitClient.getService();
    }

    public MutableLiveData<VersionDAO> getVersionData(String deviceType){
        Call<VersionDAO> versionDataCall = mInterface.getVersion(deviceType);
        versionDataCall.enqueue(new Callback<VersionDAO>() {
            @Override
            public void onResponse(Call<VersionDAO> call, Response<VersionDAO> response) {
                if(response.body()!= null){
                    LogUtil.logE("get Version : " + response.message());
                    versionData.setValue(response.body());
                }else {
                    LogUtil.logE("get Version Error " + response.message());
                }
            }

            @Override
            public void onFailure(Call<VersionDAO> call, Throwable t) {
                LogUtil.logE("get Version Error" + t);
                versionData.postValue(null);
            }
        });
        return versionData;
    }
}
