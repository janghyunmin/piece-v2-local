package com.bsstandard.piece.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *packageName    : com.bsstandard.piece.data.datasource.local.room.entity
 * fileName       : User
 * author         : piecejhm
 * date           : 2022/09/18
 * description    : Room User Database
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/18        piecejhm       최초 생성
 */

@Entity
class User(
    var name: String,
    var pinNumber: String,
    var cellPhoneNo: String,
    var birthDay: String,
    var gender: String,
    var isFido: String
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}