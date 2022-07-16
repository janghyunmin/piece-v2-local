package com.bsstandard.piece.retrofit;

import com.bsstandard.piece.widget.utils.Division;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * packageName    : com.bsstandard.piece.retrofit
 * fileName       : RetrofitConnection
 * author         : piecejhm
 * date           : 2022/07/05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/05        piecejhm       최초 생성
 */

public class RetrofitConnection {
    public static final String DEV_BASE_URL= Division.PIECE_API_V2_DEV;

    private String memberId;
    private String pinNumber;

    public RetrofitConnection(String memberId,String pinNumber) {
        this.memberId = memberId;
        this.pinNumber = pinNumber;
    }

    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("memberId",memberId)
                    .addHeader("pinNumber",pinNumber)
                    .build();
            return chain.proceed(request);
        }
    }).build();

    Retrofit retrofit = new Retrofit.Builder()
            .client(client)
            .baseUrl(DEV_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    RetrofitService retrofitService = retrofit.create(RetrofitService.class);

}
