package com.bsstandard.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *packageName    : com.bsstandard.data.source.local.entity
 * fileName       : AuthEntity
 * author         : piecejhm
 * date           : 2022/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/03        piecejhm       최초 생성
 */


@Entity(tableName = "Member")
data class AuthEntity(
    var isDi: String,
    val isWithdrawal: String,
    val isWithdrawalOver: String?
)
