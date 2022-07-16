package com.bsstandard.data.repository.local

import com.bsstandard.data.model.PortfolioInfo
import io.reactivex.Completable
import io.reactivex.Single

/**
 *packageName    : com.bsstandard.data.repository.local
 * fileName       : PortfolioLocalDataSource
 * author         : piecejhm
 * date           : 2022/07/12
 * description    : Local에 저장되어 있는 Portfolio Data를 사용하기 위한 interface
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/12        piecejhm       최초 생성
 */

interface PortfolioLocalDataSource {
    fun insertPortfolio(portfolios: List<PortfolioInfo.PortfolioResponse>) : Completable
    fun getAllPortfolios() : Single<List<PortfolioInfo.PortfolioResponse>>
    fun deleteAllPortfolios() : Completable
}