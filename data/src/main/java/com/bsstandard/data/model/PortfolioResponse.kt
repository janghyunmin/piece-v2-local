package com.bsstandard.data.model

import com.google.gson.annotations.SerializedName

/**
 *packageName    : com.bsstandard.data.model
 * fileName       : PortfolioResponse
 * author         : piecejhm
 * date           : 2022/07/12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/12        piecejhm       최초 생성
 */
class PortfolioInfo {
    data class PortfolioResponse(
        @SerializedName("status")
        val status: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("data")
        val parent: List<ParentDataEntity>
    )
    data class ParentDataEntity(
        @SerializedName("data") val data: ArrayList<DataEntity>,
        @SerializedName("totalCount") val totalCount: Int
    )
    data class DataEntity(
        @SerializedName("portfolioId") val portfolioId: String,
        @SerializedName("expectationProfitRate") val expectationProfitRate: String,
        @SerializedName("representThumbnailImagePath") val representThumbnailImagePath: String,
        @SerializedName("recruitmentState") val recruitmentState: String,
        @SerializedName("remainingPieceVolume") val remainingPieceVolume: String,
        @SerializedName("recruitmentBeginDate") val recruitmentBeginDate: String,
        @SerializedName("soldoutAt") val soldoutAt: String,
        @SerializedName("createdAt") val createdAt: String,
        @SerializedName("shareUrl") val shareUrl: String
    )
}

