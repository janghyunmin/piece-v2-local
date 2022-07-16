package com.bsstandard.data.repository

import com.bsstandard.data.mapper.Mapper.mapperToPortfolio
import com.bsstandard.data.repository.local.PortfolioLocalDataSource
import com.bsstandard.data.repository.remote.PortfolioRemoteDataSource
import com.bsstandard.domain.model.remote.Portfolio
import com.bsstandard.domain.repository.PortfolioRepository
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 *packageName    : com.bsstandard.data.repository
 * fileName       : PortfolioRepositoryImpl
 * author         : piecejhm
 * date           : 2022/07/12
 * description
 * Domain Layer 의 Repository Interface 구현부.
 * DataSource 를 인자로 받아 컨트롤 하여 필요한 Portfolio Data 를 가져옴.
 *
 * @param portfolioLocalDataSource Local 에 저장 되어있는 Data
 * @param portfolioDataSource api 에서 가져오는 Data
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/12        piecejhm       최초 생성
 */


//class PortfolioRepositoryImpl @Inject constructor(
//    private val portfolioRemoteDataSource: PortfolioRemoteDataSource,
//    private val portfolioLocalDataSource: PortfolioLocalDataSource,
//) : PortfolioRepository {
//
//
//
//    // 포트폴리오 최초 검색 - jhm 2022/07/13
//    override fun getPortfolios(): Flowable<List<Portfolio>> {
//        return portfolioLocalDataSource.getAllPortfolios()
//            .onErrorReturn { listOf() }
//            // flatMapPublisher : 함수 결과를 return
//            .flatMapPublisher { localPortfolio ->
//                if (localPortfolio.isEmpty()) { // 로컬 DB가 비어있으면
//                    getRemotePortfolio()
//                        .toFlowable()
//                        .onErrorReturn { listOf() }
//                } else {
//                    val local = Single.just(mapperToPortfolio(localPortfolio)) // 로컬 DB
//                    val remote = getRemotePortfolio() // 서버 API
//                        .onErrorResumeNext { local }
//                    Single.concat(local, remote) // 순서대로 불러옴
//                }
//            }
//    }
//
//
//
//    // 네트워크 연결이 안될 경우 로컬에서 검색 - jhm 2022/07/12
//    override fun getLocalPortfolio(): Flowable<List<Portfolio>> {
//        return portfolioLocalDataSource.getAllPortfolios()
//            .onErrorReturn { listOf() }
//            .flatMapPublisher { cachedPortfolio ->
//                if (cachedPortfolio.isEmpty()) {
//                    Flowable.error(IllegalStateException("Local Portfolio NoData.."))
//                } else {
//                    Flowable.just(mapperToPortfolio(cachedPortfolio))
//                }
//            }
//    }
//
//    // 서버에서 Portfolio 리스트 조회 요청 후 DB insert - jhm 2022/07/12
//    override fun getRemotePortfolio(): Single<List<Portfolio>> {
//        return portfolioRemoteDataSource.getPortfolio()
//            .flatMap {
//                // portfolioEntity 로 localDB에 insert - jhm 2022/07/12
//                portfolioLocalDataSource.insertPortfolio(it.parent)
//                    .andThen(Single.just(mapperToPortfolio(it.parent)))
//            }
//    }
//
//}