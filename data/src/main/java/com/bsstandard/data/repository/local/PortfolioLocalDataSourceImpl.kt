package com.bsstandard.data.repository.local

/**
 *packageName    : com.bsstandard.data.repository.local
 * fileName       : PortfolioLocalDataSourceImpl
 * author         : piecejhm
 * date           : 2022/07/12
 * description
 * DataSource 에서 선언한 Interface 의 구현부.
 * 해당 Interface 를 상속받아 사용한다.
 * Local DB 를 사용하기 때문에 Portfolio Dao 를 사용하여 데이터를 가져온다.
 *
 * @param portfolioDao Local portfolio Data 가 저장 되어있는 DB
 *
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/12        piecejhm       최초 생성
 */


//class PortfolioLocalDataSourceImpl @Inject constructor(private val portfolioDao: PortfolioDao) :
//    PortfolioLocalDataSource {
//    override fun insertPortfolio(portfolio: List<PortfolioInfo.PortfolioInfoList.PortfolioInfoChildList.DataEntity>): Completable = portfolioDao.insertPortfolio(portfolio)
//
//    override fun getAllPortfolios(): Single<List<PortfolioInfo.PortfolioInfoList.PortfolioInfoChildList.PortfolioResponse>> = portfolioDao.getAllPortfolios()
//
//    override fun deleteAllPortfolios(): Completable = portfolioDao.deleteAllPortfolio()
//}