package com.bsstandard.piece.retrofit;

import com.bsstandard.piece.widget.utils.Division;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * packageName    : com.bsstandard.piece.api
 * fileName       : ApiClient
 * author         : piecejhm
 * date           : 2022/06/10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/10        piecejhm       최초 생성
 */

public class RetrofitClient {

    public static final String DEV_BASE_URL= Division.PIECE_API_V2_DEV;
    public static final String PROD_BASE_URL = Division.PIECE_API_V2_PROD;
    private static int CONNECT_TIMEOUT = 30;
    private static int WRITE_TIMEOUT = 30;
    private static int READ_TIMEOUT = 30;

    private static Retrofit retrofit;

    public static Retrofit getInstance(){
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(DEV_BASE_URL)
                .client(getClientAuthentication())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public static RetrofitService getService(){
        return getInstance().create(RetrofitService.class);
    }

    public static OkHttpClient getClientAuthentication() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(DEV_BASE_URL);
        AuthenticationInterceptor interceptor = new AuthenticationInterceptor(
                Credentials.basic("", ""));
        if (!httpClient.interceptors().contains(interceptor)) {
            httpClient.addInterceptor(interceptor);
            httpClient.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
            httpClient.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
            httpClient.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
        }

        return httpClient.build();
    }

    public static class AuthenticationInterceptor implements Interceptor {

        private String authToken;

        public AuthenticationInterceptor(String token) {
            this.authToken = token;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            Request.Builder builder = original.newBuilder()
                    .header("Authorization", authToken);

            Request request = builder.build();
            return chain.proceed(request);
        }
    }
}