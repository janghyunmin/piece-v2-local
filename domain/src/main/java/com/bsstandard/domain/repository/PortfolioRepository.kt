package com.bsstandard.domain.repository

import com.bsstandard.domain.model.remote.Portfolio
import io.reactivex.Flowable
import io.reactivex.Single

/**
 *packageName    : com.bsstandard.domain.repository
 * fileName       : PortfolioRepository
 * author         : piecejhm
 * date           : 2022/07/11
 * description
 * UseCase 에 필요한 Interface 를 선한한 Repository.
 * RepositoryImpl 에서 구현되며, 실제 필요한 데이터를 가져온다.
 *
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/11        piecejhm       최초 생성
 */
interface PortfolioRepository {
   fun getPortfolioList(): Flowable<List<Portfolio>>
}