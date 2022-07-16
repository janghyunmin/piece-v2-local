package com.bsstandard.piece.data.repository

import com.bsstandard.piece.data.datasource.local.room.dao.PortfolioDAO
import com.bsstandard.piece.model.PortfolioResponse
import javax.inject.Inject

/**
 *packageName    : com.bsstandard.piece.data.repository
 * fileName       : RoomRepository
 * author         : piecejhm
 * date           : 2022/07/13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/13        piecejhm       최초 생성
 */


class RoomRepository @Inject constructor(private val portfolioDao : PortfolioDAO) {

    fun insertRecords(portfolioResponse: PortfolioResponse) {
        portfolioDao.insertRecords(portfolioResponse)
    }

    fun getDataUserById(portfolioId: String): PortfolioResponse {
        return portfolioDao.getDataUserById(portfolioId)
    }

    fun updateRecord(portfolioResponse: PortfolioResponse) {
        portfolioDao.updateRecord(portfolioResponse)
    }

    fun deleteRecord(portfolioResponse: PortfolioResponse) {
        portfolioDao.deleteRecord(portfolioResponse)
    }
}