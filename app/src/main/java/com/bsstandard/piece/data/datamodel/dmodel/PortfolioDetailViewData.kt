package com.bsstandard.piece.data.datamodel.dmodel

import java.util.*

/**
 *packageName    : com.bsstandard.piece.data.datamodel.dmodel
 * fileName       : PortfolioDetailViewData
 * author         : piecejhm
 * date           : 2022/08/22
 * description    : 포트폴리오 상세 구성 RecyclerAdapter + 포트폴리오 상세 하단 구성 layout RecyclerAdapter ViewType 분기
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/22        piecejhm       최초 생성
 */
data class PortfolioDetailViewData(
    val productId: String,
    val title: String,
    val representThumbnailImagePath: String,
    val productionYear: String,
    val productMaterial: String,
    val productSize: String,
    val productDetailInfo: String,
    val productDocuments: Objects,
    val type: Int
)
