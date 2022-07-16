package com.bsstandard.domain.usecase

import com.bsstandard.domain.repository.PortfolioRepository
import com.bsstandard.domain.utils.RemoteErrorEmitter
import java.security.acl.Owner
import javax.inject.Inject

/**
 *packageName    : com.bsstandard.domain.usecase
 * fileName       : GetUserPortfolioUseCase
 * author         : piecejhm
 * date           : 2022/07/11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/11        piecejhm       최초 생성
 */
//class GetUserPortfolioUseCase @Inject constructor(
//    private val portfolioRepository: PortfolioRepository
//) {
//    suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter, owner: String) = portfolioRepository.getLocalPortfolio(remoteErrorEmitter, owner)
//}