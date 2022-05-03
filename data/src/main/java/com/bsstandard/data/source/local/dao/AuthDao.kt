package com.bsstandard.data.source.local.dao

import androidx.room.*
import com.bsstandard.data.source.local.entity.AuthEntity

/**
 *packageName    : com.bsstandard.data.source.local.dao
 * fileName       : AuthDao
 * author         : piecejhm
 * date           : 2022/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/03        piecejhm       최초 생성
 */

@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(authEntity: AuthEntity) : Long

    @Query("SELECT * FROM Member")
    fun loadAll(): MutableList<AuthEntity>

    @Delete
    fun delete(authEntity: AuthEntity)

    @Query("DELETE FROM Member")
    fun deleteAll()

    @Query("SELECT * FROM Member where id = :isDi")
    fun loadOneByIsDi(idDi: Long): AuthEntity?

    @Update
    fun update(authEntity: AuthEntity)
}