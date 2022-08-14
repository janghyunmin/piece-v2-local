package com.bsstandard.piece.data.datasource.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *packageName    : com.bsstandard.piece.data.datasource.local.room.entity
 * fileName       : Portfolio
 * author         : piecejhm
 * date           : 2022/07/08
 * description    : RoomDB 포트폴리오 Entity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/08        piecejhm       최초 생성
 */

@Entity(tableName = "port_folio")
data class Portfolio (
    @PrimaryKey
    @ColumnInfo(name = "portfolioId")
    val portfolioId: String,

    @ColumnInfo(name = "expectationProfitRate")
    val expectationProfitRate: String,

    @ColumnInfo(name = "representThumbnailImagePath")
    val representThumbnailImagePath: String,

    @ColumnInfo(name = "recruitmentState")
    val recruitmentState: String,

    @ColumnInfo(name = "remainingPieceVolume")
    val remainingPieceVolume: String,

    @ColumnInfo(name = "recruitmentBeginDate")
    val recruitmentBeginDate: String,

    @ColumnInfo(name = "soldoutAt")
    val soldoutAt: String,

    @ColumnInfo(name = "createdAt")
    val createdAt: String,

    @ColumnInfo(name = "shareUrl")
    val shareUrl: String
)