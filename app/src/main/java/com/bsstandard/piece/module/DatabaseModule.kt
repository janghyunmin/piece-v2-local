package com.bsstandard.piece.module

import android.app.Application
import androidx.room.Room
import com.bsstandard.data.source.local.AppDatabase
import com.bsstandard.data.source.local.dao.AuthDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 *packageName    : com.bsstandard.piece.module
 * fileName       : DataSourceImplModule
 * author         : piecejhm
 * date           : 2022/05/02
 * description    : 공통 API Data
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/02        piecejhm       최초 생성
 */

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    internal fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).allowMainThreadQueries().build()
    }

    @Provides
    internal fun provideAuthDao(appDatabase: AppDatabase): AuthDao {
        return appDatabase.authDao()
    }
}