package com.bsstandard.domain.usecase

import com.bsstandard.domain.model.local.Auth
import com.bsstandard.domain.repository.AuthRepository
import com.bsstandard.domain.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

/**
 *packageName    : com.bsstandard.domain.usecase
 * fileName       : GetAuthUseCase
 * author         : piecejhm
 * date           : 2022/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/03        piecejhm       최초 생성
 */


class GetAuthUseCase @Inject constructor(
    private val repository: AuthRepository
) : SingleUseCase<Auth>() {

    private var isDi: Long? = null

    fun getAuth(is_Di: Long){
        isDi = is_Di
    }

    override fun buildUseCaseSingle(): Single<Auth> {
        return repository.getAuth(isDi)
    }
}
