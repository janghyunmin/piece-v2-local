package com.bsstandard.piece.data.datasource.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *packageName    : com.bsstandard.piece.data.datasource.room.entity
 * fileName       : Version
 * author         : piecejhm
 * date           : 2022/06/10
 * description    : 앱 버전 조회 entity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/10        piecejhm       최초 생성
 */

@Entity
class Version (
    @PrimaryKey(autoGenerate = true) var id: Long,
    var version: String
)