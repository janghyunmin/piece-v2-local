package com.bsstandard.piece.data.datasource.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bsstandard.piece.data.datasource.local.room.dao.UserDao
import com.bsstandard.piece.data.datasource.local.room.entity.User

/**
 *packageName    : com.bsstandard.piece.data.datasource.local.room
 * fileName       : AppDatabase
 * author         : piecejhm
 * date           : 2022/07/08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/08        piecejhm       최초 생성
 */

/**
여러 엔터티(model)를 사용하는 경우
entities = [RoomTodoData::class, OtherData::class] <- 추가합니다.

abstract fun TodoDao(): RetrofitTodoData
abstract fun otherDao(): OtherData <- 추가합니다.

앱이 깔린 상태에서 엔터티(model)를 수정한 경우 : 앱을 다시 깔거나 버전을 올려줍니다.
 **/

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "piece-local-database"
                    ).build()
                }
            }
            return instance
        }
    }
}