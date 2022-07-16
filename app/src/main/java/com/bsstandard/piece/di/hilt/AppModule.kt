package com.bsstandard.piece.di.hilt

import android.app.Application
import com.bsstandard.piece.data.datasource.local.room.AppDatabase
import com.bsstandard.piece.data.datasource.local.room.dao.PortfolioDAO
import com.bsstandard.piece.retrofit.RetrofitClient
import com.bsstandard.piece.retrofit.RetrofitClient.AuthenticationInterceptor
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.widget.utils.Division
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 *packageName    : com.bsstandard.piece.di.hilt
 * fileName       : AppModule
 * author         : piecejhm
 * date           : 2022/07/10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/10        piecejhm       최초 생성
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }


    @Provides
    @Singleton
    fun getAppDatabase(context: Application): AppDatabase {
        return AppDatabase.getAppDBInstance(context)
    }

    @Provides
    @Singleton
    fun getAppDao(appDatabase: AppDatabase): PortfolioDAO {
        return appDatabase.getPortfolioDao()
    }

    val BASE_URL = Division.PIECE_API_V2_DEV
    //val BASE_URL = Division.PIECE_API_V2_PROD


    @Provides
    @Singleton
    fun getRetroInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClientAuthentication())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun getRetroServiceInstance(retrofit: Retrofit): RetrofitService {
        return retrofit.create(RetrofitService::class.java)
    }

    fun getClientAuthentication(): OkHttpClient? {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        val builder = Retrofit.Builder()
            .baseUrl(RetrofitClient.DEV_BASE_URL)
        val interceptor = AuthenticationInterceptor(
            Credentials.basic("", "")
        )
        if (!httpClient.interceptors().contains(interceptor)) {
            httpClient.addInterceptor(interceptor)
            httpClient.connectTimeout(30, TimeUnit.SECONDS)
            httpClient.writeTimeout(30, TimeUnit.SECONDS)
            httpClient.readTimeout(30, TimeUnit.SECONDS)
            httpClient.addInterceptor(logging)
            builder.client(httpClient.build())
        }
        return httpClient.build()
    }







}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope