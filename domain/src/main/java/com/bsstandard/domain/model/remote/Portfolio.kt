package com.bsstandard.domain.model.remote

/**
 *packageName    : com.bsstandard.domain.model.remote
 * fileName       : PortfolioResponse
 * author         : piecejhm
 * date           : 2022/07/11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/11        piecejhm       최초 생성
 */

data class Portfolio(
    val portfolioId: String,
    val expectationProfitRate: String,
    val representThumbnailImagePath: String,
    val recruitmentState: String,
    val remainingPieceVolume: String,
    val recruitmentBeginDate: String,
    val soldoutAt: String,
    val createdAt: String,
    val shareUrl: String
)