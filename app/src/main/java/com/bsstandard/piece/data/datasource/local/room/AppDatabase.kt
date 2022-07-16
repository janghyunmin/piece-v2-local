package com.bsstandard.piece.data.datasource.local.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bsstandard.piece.data.datasource.local.room.dao.PortfolioDAO
import com.bsstandard.piece.model.PortfolioResponse
import com.bsstandard.piece.model.TypeConverterData

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

@Database(entities = [PortfolioResponse::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverterData::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getPortfolioDao(): PortfolioDAO

    companion object {
        private var DB_INSTANCE: AppDatabase? = null

        fun getAppDBInstance(context: Context): AppDatabase {
            if (DB_INSTANCE == null) {
                DB_INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "PIECE_DB"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            Log.d("logging", "DB created")
            return DB_INSTANCE!!
        }
    }
}
