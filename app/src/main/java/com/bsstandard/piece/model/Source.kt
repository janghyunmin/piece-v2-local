package com.bsstandard.piece.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *packageName    : com.bsstandard.piece.model
 * fileName       : Portfolio
 * author         : piecejhm
 * date           : 2022/07/10
 * description    : 포트폴리오 api data class
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/10        piecejhm       최초 생성
 */

@Parcelize
data class Source(
    val portfolioId: String,
    val expectationProfitRate: String,
    val representThumbnailImagePath: String,
    val recruitmentState: String,
    val remainingPieceVolume: String,
    val recruitmentBeginDate: String,
    val soldoutAt: String,
    val createdAt: String,
    val shareUrl: String,
): Parcelable
