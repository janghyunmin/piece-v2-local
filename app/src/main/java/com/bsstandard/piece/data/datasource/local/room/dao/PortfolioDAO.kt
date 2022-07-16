package com.bsstandard.piece.data.datasource.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bsstandard.piece.model.PortfolioResponse

/**
 *packageName    : com.bsstandard.piece.data.datasource.local.room.dao
 * fileName       : PortfolioDAO
 * author         : piecejhm
 * date           : 2022/07/08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/08        piecejhm       최초 생성
 */

@Dao
interface PortfolioDAO {

    @Query("SELECT * FROM port_folio")
    fun getAllRecords(): LiveData<List<PortfolioResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecords(portfolio: PortfolioResponse)

    @Query("DELETE FROM port_folio")
    fun deleteAllRecords()

    @Query("SELECT * FROM port_folio WHERE portfolioId = :id")
    fun getDataUserById(id: String): PortfolioResponse

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateRecord(portfolio: PortfolioResponse)

    @Delete
    fun deleteRecord(portfolio: PortfolioResponse)


}