package com.bsstandard.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

/**
 *packageName    : com.bsstandard.data.db
 * fileName       : PortfolioDao
 * author         : piecejhm
 * date           : 2022/07/12
 * description    : 필요한 데이터를 DB에서 가지고 오기 위한 쿼리를 작성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/12        piecejhm       최초 생성
 */


@Dao
interface PortfolioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPortfolio(portfolio: List<DataEntity>) : Completable

    @Query("SELECT * FROM portfolio")
    fun getAllPortfolios(): Single<List<DataEntity>>

    @Query("SELECT * FROM portfolio WHERE portfolioId")
    fun getPortfolioDetail(portfolioId: String): Single<List<DataEntity>>

    @Query("DELETE FROM portfolio")
    fun deleteAllPortfolio(): Completable


}