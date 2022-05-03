package com.bsstandard.piece.module

import com.bsstandard.domain.repository.AuthRepository
import com.bsstandard.domain.usecase.GetAuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *packageName    : com.bsstandard.piece.module
 * fileName       : UseCaseModule
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
class UseCaseModule {
    @Provides
    @Singleton
    fun provideGetAuthCase(repository: AuthRepository) = GetAuthUseCase(repository)

}
