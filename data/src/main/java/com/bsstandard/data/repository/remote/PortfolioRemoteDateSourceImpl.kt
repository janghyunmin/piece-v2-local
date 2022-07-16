package com.bsstandard.data.repository.remote

import com.bsstandard.data.api.ApiInterface
import com.bsstandard.data.model.PortfolioInfo
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 *packageName    : com.bsstandard.data.repository.remote
 * fileName       : PortfolioRemoteDateSourceImpl
 * author         : piecejhm
 * date           : 2022/07/12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/12        piecejhm       최초 생성
 */
class PortfolioRemoteDateSourceImpl @Inject constructor(private val apiInterface: ApiInterface) :
    PortfolioRemoteDataSource {
    override fun getPortfolio(): Single<PortfolioInfo.PortfolioResponse> {
        return apiInterface.getPortfolio()
    }

    override fun getPortfolioFlow(): Flow<PortfolioInfo.PortfolioResponse> {
        return flow {
            emit(apiInterface.getPortfolioFlow())
        }
    }
}