package com.bsstandard.piece.module

import com.bsstandard.data.repository.AuthRepositoryImpl
import com.bsstandard.data.source.local.AppDatabase
import com.bsstandard.data.source.remote.RetrofitService
import com.bsstandard.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *packageName    : com.bsstandard.piece.module
 * fileName       : RepositoryModule
 * author         : piecejhm
 * date           : 2022/05/02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/02        piecejhm       최초 생성
 */


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        appDatabase: AppDatabase,
        retrofitService: RetrofitService
    ) : AuthRepository {
        return AuthRepositoryImpl(
            appDatabase,retrofitService
        )
    }
}
