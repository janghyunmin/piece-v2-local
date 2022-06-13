package com.bsstandard.piece

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bsstandard.piece.data.datasource.room.AppDatabase

/**
 *packageName    : com.bsstandard.piece
 * fileName       : App
 * author         : piecejhm
 * date           : 2022/06/10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/10        piecejhm       최초 생성
 */

class App : Application() {
    companion object {
        lateinit var appInstance: App
            private set

//        lateinit var appDataBaseInstance: AppDatabase
//            private set

    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this

//        appDataBaseInstance = Room.databaseBuilder(
//            appInstance.applicationContext,
//            AppDatabase::class.java,"PIECE.DB"
//        )
//            .fallbackToDestructiveMigration() // DB version 달라졌을 경우 데이터베이스 초기화 - jhm 2022/06/10
//            .allowMainThreadQueries() // 메인 스레드에서 접근 허용 - jhm 2022/06/10
//            .build()
    }
}