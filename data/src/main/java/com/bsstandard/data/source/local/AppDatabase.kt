package com.bsstandard.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bsstandard.data.source.local.dao.AuthDao
import com.bsstandard.data.source.local.entity.AuthEntity

/**
 *packageName    : com.bsstandard.data.source.local
 * fileName       : AppDatabase
 * author         : piecejhm
 * date           : 2022/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/03        piecejhm       최초 생성
 */


@Database(entities = [AuthEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authDao(): AuthDao

    companion object {
        const val DB_NAME = "PieceDatabase"
        private var database: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (database == null) {
                database = create(context)
            }
            return database!!
        }

        fun getInstanceInMemory(context: Context?): AppDatabase {
            return Room.inMemoryDatabaseBuilder(
                context!!,
                AppDatabase::class.java
            )
                .build()
        }

        private fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java, DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        fun removeDataBase(context: Context): Boolean {
            val dbFile = context.getDatabasePath(DB_NAME)
            return if (dbFile.exists()) {
                dbFile.delete()
            } else {
                false
            }
        }
    }
}