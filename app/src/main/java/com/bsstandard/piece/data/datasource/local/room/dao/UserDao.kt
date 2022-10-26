package com.bsstandard.piece.data.datasource.local.room.dao

import androidx.room.*
import com.bsstandard.piece.data.datasource.local.room.entity.User

/**
 *packageName    : com.bsstandard.piece.data.datasource.local.room.dao
 * fileName       : UserDao
 * author         : piecejhm
 * date           : 2022/09/18
 * description    : Room User Dao
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/18        piecejhm       최초 생성
 */

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM User") // User 테이블 조회 - jhm 2022/09/18
    fun getAll(): List<User>

    @Query("SELECT * FROM User WHERE name = :name AND isFido = :isFido")
    fun getIsFido(name: String, isFido: String) : List<User>

    @Query("DELETE FROM User WHERE name = :name") // 'name'에 해당하는 유저를 삭제해라 - jhm 2022/09/18
    fun deleteUserByName(name: String)

    @Query("UPDATE User SET isFido = :isFido WHERE name = :name")
    fun updateByIsFido(isFido: String , name: String)
}
