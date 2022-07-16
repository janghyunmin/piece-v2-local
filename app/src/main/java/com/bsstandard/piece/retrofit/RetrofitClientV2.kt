package com.bsstandard.piece.retrofit

import com.bsstandard.piece.widget.utils.Division
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *packageName    : com.bsstandard.piece.retrofit
 * fileName       : RetrofitServiceV2
 * author         : piecejhm
 * date           : 2022/07/14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/14        piecejhm       최초 생성
 */

object RetrofitClientV2 {
    // 서버 주소
    private const val BASE_URL = Division.PIECE_API_V2_DEV
    var token: String = ""

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30,TimeUnit.SECONDS)
        .writeTimeout(30,TimeUnit.SECONDS)
        .readTimeout(30,TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE
    }).addInterceptor {
        // Request
//        val request = it.request()
//            .newBuilder()
//            .addHeader("Authorization", "Bearer $token")
//            .build()

        val request = it.request()
            .newBuilder()
            .build()


        // Response
        val response = it.proceed(request)
        response
    }.build()


    private val getRetrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    val retrofitService : RetrofitService by lazy{
        getRetrofit.create(RetrofitService::class.java)
    }
}
