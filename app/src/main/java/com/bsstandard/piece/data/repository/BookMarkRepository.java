//package com.bsstandard.piece.data.repository;
//
//import android.app.Application;
//
//import androidx.lifecycle.MutableLiveData;
//
//import com.bsstandard.piece.data.datasource.shared.PrefsHelper;
//import com.bsstandard.piece.data.dto.BookMarkDTO;
//import com.bsstandard.piece.di.hilt.ApiModule;
//import com.bsstandard.piece.retrofit.RetrofitService;
//import com.bsstandard.piece.widget.utils.LogUtil;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * packageName    : com.bsstandard.piece.data.repository
// * fileName       : BookMarkRepository
// * author         : piecejhm
// * date           : 2022/08/31
// * description    :
// * ===========================================================
// * DATE              AUTHOR             NOTE
// * -----------------------------------------------------------
// * 2022/08/31        piecejhm       최초 생성
// */
//public class BookMarkRepository {
//    private static RetrofitService mInstance;
//    private static BookMarkRepository bookMarkRepository;
//    private final MutableLiveData<BookMarkDTO> bookMarkData = new MutableLiveData<>();
//
//    private String accessToken = PrefsHelper.read("accessToken","");
//    private String deviceId = PrefsHelper.read("deviceId","");
//    private String memberId = PrefsHelper.read("memberId","");
//
//    public static BookMarkRepository getInstance(Application application) {
//        if(bookMarkRepository == null) {
//            bookMarkRepository = new BookMarkRepository(application);
//        }
//        return bookMarkRepository;
//    }
//
//    public BookMarkRepository(Application application) {
//        mInstance = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);
//    }
//
//    public MutableLiveData<BookMarkDTO> getBookMarkData() {
//        Call<BookMarkDTO> bookMarkCall = mInstance.getBookMark("Bearer " + accessToken , deviceId, memberId);
//        bookMarkCall.enqueue(new Callback<BookMarkDTO>() {
//            @Override
//            public void onResponse(Call<BookMarkDTO> call, Response<BookMarkDTO> response) {
//                if(response.isSuccessful()) {
//                    LogUtil.logE("get BookMark list Success ! " + response.body().getData().size());
//                    bookMarkData.postValue(response.body());
//
//                } else {
////                    LogUtil.logE("get BookMark list Error !");
//                    bookMarkData.setValue(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BookMarkDTO> call, Throwable t) {
//                LogUtil.logE("get BookMark Fail.." + t.getMessage());
//                bookMarkData.postValue(null);
//            }
//        });
//        return bookMarkData;
//    }
//}
