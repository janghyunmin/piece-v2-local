package com.bsstandard.piece.data.repository;

import android.app.Application;

import com.bsstandard.piece.data.datamodel.dmodel.member.MemberModifyModel;
import com.bsstandard.piece.data.datasource.shared.PrefsHelper;
import com.bsstandard.piece.data.dto.MemberPutDTO;
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
 * fileName       : MemberUpdateRepository
 * author         : piecejhm
 * date           : 2022/09/05
 * description    : 회원 정보 수정 요청 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/05        piecejhm       최초 생성
 */
public class MemberUpdateRepository {
    private static RetrofitService mInstance;
    private static MemberUpdateRepository memberUpdateRepository;
    private final SingleLiveEvent<MemberPutDTO> memberUpdateData = new SingleLiveEvent<>();

    public static MemberUpdateRepository getInstance(Application application) {
        if(memberUpdateRepository == null) {
            memberUpdateRepository = new MemberUpdateRepository(application);
        }
        return memberUpdateRepository;
    }

    public MemberUpdateRepository(Application application) {
        mInstance = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);
    }
    public SingleLiveEvent<MemberPutDTO> putMemberData(MemberModifyModel memberModifyModel) {
        String accessToken = PrefsHelper.read("accessToken","");
        String deviceId = PrefsHelper.read("deviceId","");
        String memberId = PrefsHelper.read("memberId","");

        HashMap<String,String> map = new HashMap<>();
        map.put("accessToken","Bearer " + accessToken);
        map.put("deviceId",deviceId);
        map.put("memberId",memberId);

        Call<MemberPutDTO> putMemberData = mInstance.putMember(map,memberModifyModel);
        putMemberData.enqueue(new Callback<MemberPutDTO>() {
            @Override
            public void onResponse(Call<MemberPutDTO> call, Response<MemberPutDTO> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus().equals("OK")) {
                        LogUtil.logE("회원 정보 변경 성공");
                        memberUpdateData.postValue(response.body());
                    } else {
                        LogUtil.logE("회원 정보 변경 실패..");
                        memberUpdateData.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<MemberPutDTO> call, Throwable t) {
                LogUtil.logE("put /member error..." + t.getMessage());
                memberUpdateData.postValue(null);
            }
        });

        return memberUpdateData;
    }
}
