package com.bsstandard.domain.repository

import com.bsstandard.domain.model.local.Auth
import io.reactivex.Single

/**
 *packageName    : com.bsstandard.domain.repository
 * fileName       : AuthRepository
 * author         : piecejhm
 * date           : 2022/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/03        piecejhm       최초 생성
 */
interface AuthRepository {
    fun getAuth(isDi: Long?): Single<Auth>
}