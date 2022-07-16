package com.bsstandard.data.repository.remote

import com.bsstandard.data.model.PortfolioInfo
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

/**
 *packageName    : com.bsstandard.data.repository.remote
 * fileName       : PortfolioRemoteDataSource
 * author         : piecejhm
 * date           : 2022/07/12
 * description
 * Api 호출을 통해 Portfolio Data 를 가져오기 위한 interface
 * DataSourceImpl 에서 구현된다.
 *
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/12        piecejhm       최초 생성
 */
interface PortfolioRemoteDataSource {
    fun getPortfolio() : Single<PortfolioInfo.PortfolioResponse>
    fun getPortfolioFlow(): Flow<PortfolioInfo.PortfolioResponse>
}