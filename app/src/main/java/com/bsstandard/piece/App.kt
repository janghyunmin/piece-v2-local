package com.bsstandard.piece

import android.app.Application
import com.bsstandard.piece.di.hilt.ApiModule
import com.bumptech.glide.module.AppGlideModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 *packageName    : com.bsstandard.piece
 * fileName       : App
 * author         : piecejhm
 * date           : 2022/06/10
 * description    : Dagger Hilt DI
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/10        piecejhm       최초 생성
 */

@HiltAndroidApp
class App : Application() {
    companion object {
        private lateinit var application: Application
        fun getInstance(): Application = application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}