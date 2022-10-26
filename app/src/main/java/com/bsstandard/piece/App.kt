package com.bsstandard.piece

import android.app.Application
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.widget.utils.LogUtil
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp

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
        PrefsHelper.init(applicationContext)
        application = this

        initFirebase()
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                LogUtil.logE("token : ${task.result}")
                PrefsHelper.write("fcmToken",task.result)
            }
        }
    }
}