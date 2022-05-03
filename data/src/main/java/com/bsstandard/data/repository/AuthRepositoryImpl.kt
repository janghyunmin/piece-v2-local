package com.bsstandard.data.repository

import com.bsstandard.data.source.local.AppDatabase
import com.bsstandard.data.source.remote.RetrofitService
import com.bsstandard.domain.model.local.Auth
import com.bsstandard.domain.repository.AuthRepository
import io.reactivex.Single

/**
 *packageName    : com.bsstandard.data.repository
 * fileName       : AuthRepositoryImpl
 * author         : piecejhm
 * date           : 2022/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/03        piecejhm       최초 생성
 */
class AuthRepositoryImpl(
    private val database: AppDatabase,
    private val retrofitService: RetrofitService
) : AuthRepository {

    override fun getAuth(isDi: Long?): Single<Auth> {
        TODO("Not yet implemented")
    }
}