package com.bsstandard.piece.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 *packageName    : com.bsstandard.piece.app
 * fileName       : App
 * author         : piecejhm
 * date           : 2022/05/02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/02        piecejhm       최초 생성
 */


@HiltAndroidApp
class App : Application(){
    companion object {
        private lateinit var application: App
        fun getInstance() : App = application
    }

    override fun onCreate(){
        super.onCreate()
        application = this
    }
}