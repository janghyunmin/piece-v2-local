package com.bsstandard.piece.data.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSON;
import com.bsstandard.piece.data.datamodel.dmodel.join.JoinModel;
import com.bsstandard.piece.data.dto.JoinDTO;
import com.bsstandard.piece.di.hilt.ApiModule;
import com.bsstandard.piece.retrofit.RetrofitService;
import com.bsstandard.piece.widget.utils.LogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * packageName    : com.bsstandard.piece.data.repository
 * fileName       : JoinRepository
 * author         : piecejhm
 * date           : 2022/06/30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/30        piecejhm       최초 생성
 */
public class JoinRepository {
    private static RetrofitService mInstance;
    private static JoinRepository joinRepository;
    private final MutableLiveData<JoinDTO> joinData = new MutableLiveData<>();

    public static JoinRepository getInstance(Application application) {
        if(joinRepository == null){
            joinRepository = new JoinRepository(application);
        }
        return joinRepository;
    }
    public JoinRepository(Application application) {
//        mInstance = RetrofitClient.getService();
        mInstance = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);
    }

    public MutableLiveData<JoinDTO> getJoinData(JoinModel joinModel){
        Call<JoinDTO> joinVOCall = mInstance.PostMemberJoin(joinModel);
        joinVOCall.enqueue(new Callback<JoinDTO>() {
            @Override
            public void onResponse(Call<JoinDTO> call, Response<JoinDTO> response) {
                if(response.body()!=null){
                    LogUtil.logE("회원가입 진행 response data : " + JSON.toJSONString(response.body().getData()));
                    joinData.postValue(response.body());
                }else{
                    LogUtil.logE("join response body null.. : " + response.code());
                    joinData.setValue(null);
                }


            }

            @Override
            public void onFailure(Call<JoinDTO> call, Throwable t) {
                LogUtil.logE("post /member/join error... " + t);
                joinData.postValue(null);
            }
        });
        return joinData;
    }
}
