package com.bsstandard.piece.data.datasource.room.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bsstandard.piece.data.datasource.room.entity.Version

/**
 *packageName    : com.bsstandard.piece.data.datasource.room.dao
 * fileName       : VersionDAO
 * author         : piecejhm
 * date           : 2022/06/10
 * description    : VersionDAO 담당 클래스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/10        piecejhm       최초 생성
 */


@Dao
interface VersionDAO {
    @Insert
    suspend fun insertVersion(version: Version): Long

    @Delete
    suspend fun deleteVersion(version: Version)

    @Query("DELETE FROM Version Where id = :id")
    suspend fun deleteVersionByID(id: Long)

    @Query("SELECT * FROM Version")
    suspend fun getAllVersion(): List<Version>

    @Query("UPDATE Version SET version = :version WHERE id = :id")
    suspend fun modifyVersion(version:String , id:Long)

}