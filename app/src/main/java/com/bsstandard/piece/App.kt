package com.bsstandard.piece

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.MyLifecycleObserver
import com.bsstandard.piece.widget.utils.NetworkConnection
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
class App : Application(), LifecycleOwner {
    companion object {
        private lateinit var application: Application
        private lateinit var lifecycleRegistry: LifecycleRegistry
        fun getInstance(): Application = application
    }

    override fun onCreate() {
        super.onCreate()
        PrefsHelper.init(applicationContext)

        application = this
        initFirebase() // App 시작시 Firebase Token 을 받아온 후 저장 - jhm 2022/11/03
        
        
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.addObserver(MyLifecycleObserver())
        lifecycle.addObserver(MyLifecycleObserver())

        val connection = NetworkConnection(applicationContext)
        connection.observe(this, Observer { isConnected ->
            if(isConnected) {
                LogUtil.logE("네트워크 연결되어있음")
            } else {
                LogUtil.logE("네트워크 연결 실패")
            }
        })


    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                LogUtil.logE("token : ${task.result}")
                PrefsHelper.write("fcmToken",task.result)
            }
        }
    }



    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

}