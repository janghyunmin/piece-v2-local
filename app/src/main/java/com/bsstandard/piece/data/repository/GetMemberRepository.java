//package com.bsstandard.piece.data.repository;
//
//import android.app.Application;
//
//import androidx.lifecycle.MutableLiveData;
//
//import com.bsstandard.piece.data.datasource.shared.PrefsHelper;
//import com.bsstandard.piece.data.dto.MemberDTO;
//import com.bsstandard.piece.di.hilt.ApiModule;
//import com.bsstandard.piece.retrofit.RetrofitService;
//import com.bsstandard.piece.widget.utils.LogUtil;
//import com.google.gson.Gson;
//
//import java.util.HashMap;
//
//import io.reactivex.Observer;
//import io.reactivex.Scheduler;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
//import io.reactivex.rxjava3.schedulers.Schedulers;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * packageName    : com.bsstandard.piece.data.repository
// * fileName       : GetMemberRepository
// * author         : piecejhm
// * date           : 2022/09/03
// * description    :
// * ===========================================================
// * DATE              AUTHOR             NOTE
// * -----------------------------------------------------------
// * 2022/09/03        piecejhm       최초 생성
// */
//public class GetMemberRepository {
//    private static RetrofitService mInstance;
//    private static GetMemberRepository getMemberRepository;
//    private final MutableLiveData<MemberDTO> memberData = new MutableLiveData<>();
//
//    private String accessToken  = PrefsHelper.read("accessToken","");
//    private String deviceId = PrefsHelper.read("deviceId","");
//    private String memberId = PrefsHelper.read("memberId","");
//
//    public static GetMemberRepository getInstance(Application application) {
//        if(getMemberRepository == null) {
//            getMemberRepository = new GetMemberRepository(application);
//        }
//        return getMemberRepository;
//    }
//
//    public GetMemberRepository(Application application) {
//        mInstance = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);
//    }
//
//    public MutableLiveData<MemberDTO> getMemberData() {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("accessToken","Bearer " + accessToken);
//        map.put("deviceId",deviceId);
//        map.put("memberId",memberId);
//
//
//       mInstance.getMember(map)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(Schedulers.io())
//                .observeOn(Schedulers.computation())
//                .observeOn(Schedulers.newThread())
//                .subscribe(new Observer<MemberDTO>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(MemberDTO memberDTO) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                })
////        getAuthToken.enqueue(new Callback<MemberDTO>() {
////            @Override
////            public void onResponse(retrofit2.Call<MemberDTO> call, Response<MemberDTO> response) {
////                if(response.isSuccessful()) {
////                    memberData.postValue(response.body());
////
////                    // 회원 정보 조회 Model Shared 저장 - jhm 2022/09/06
////                    Gson gson = new Gson();
////                    String memberObj = gson.toJson(response.body().getData());
////                    PrefsHelper.write("memberObj",memberObj);
////
////                    PrefsHelper.write("memberId", response.body().getData().getMemberId());
////                    PrefsHelper.write("name", response.body().getData().getName());
////                    PrefsHelper.write("pinNumber", PrefsHelper.read("pinNumber",""));
////                    PrefsHelper.write("cellPhoneNo", response.body().getData().getCellPhoneNo());
////                    PrefsHelper.write("cellPhoneIdNo", response.body().getData().getCellPhoneIdNo());
////                    PrefsHelper.write("birthDay", response.body().getData().getBirthDay());
////                    PrefsHelper.write("zipCode", "");
////                    PrefsHelper.write("baseAddress", response.body().getData().getBaseAddress().toString());
////                    PrefsHelper.write("detailAddress", response.body().getData().getDetailAddress().toString());
////                    PrefsHelper.write("ci", response.body().getData().getCi());
////                    PrefsHelper.write("di", response.body().getData().getDi());
////                    PrefsHelper.write("gender", response.body().getData().getGender());
////                    PrefsHelper.write("email", response.body().getData().getEmail().toString());
////                    PrefsHelper.write("isFido", response.body().getData().getIsFido());
////                    PrefsHelper.write("notificationMemberId", response.body().getData().getNotification().getMemberId());
////                    PrefsHelper.write("notificationAsset", response.body().getData().getNotification().getAssetNotification());
////                    PrefsHelper.write("notificationPortfolio", response.body().getData().getNotification().getPortfolioNotification());
////                    PrefsHelper.write("notificationMarketingSms", response.body().getData().getNotification().getMarketingSms());
////                    PrefsHelper.write("notificationMarketingApp", response.body().getData().getNotification().getMarketingApp());
////                    PrefsHelper.writeList("consentList", response.body().getData().getConsents());
////                } else {
////                    memberData.postValue(response.body());
////                }
////            }
////
////            @Override
////            public void onFailure(Call<MemberDTO> call, Throwable t) {
////                LogUtil.logE("get /member error.." + t);
////                memberData.postValue(null);
////            }
////        });
//        return memberData;
//    }
//}
