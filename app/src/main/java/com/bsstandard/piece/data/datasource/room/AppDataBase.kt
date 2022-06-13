package com.bsstandard.piece.data.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bsstandard.piece.data.datasource.room.dao.VersionDAO
import com.bsstandard.piece.data.datasource.room.entity.Version


/**
 *packageName    : com.bsstandard.piece.data.datasource.room
 * fileName       : AppDataBase
 * author         : piecejhm
 * date           : 2022/06/10
 * description    : 전체 RoomDataBase 로 사용하기 위한 클래스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/10        piecejhm       최초 생성
 */

@Database(entities = [Version::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun versionDao(): VersionDAO
}