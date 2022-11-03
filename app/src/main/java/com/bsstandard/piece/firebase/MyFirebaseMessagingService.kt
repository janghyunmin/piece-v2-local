package com.bsstandard.piece.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.bsstandard.piece.R
import com.bsstandard.piece.view.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

/**
 *packageName    : com.bsstandard.piece.firebase
 * fileName       : MyFirebaseMessagingService
 * author         : piecejhm
 * date           : 2022/10/18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/18        piecejhm       최초 생성
 */

class MyFirebaseMessagingService:FirebaseMessagingService() {
    private val TAG = "FirebaseService"

    /**
     * FirebaseInstanceIdService is deprecated.
     * this is new on firebase-messaging:17.1.0
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "new Token: $token")
    }

    /**
     * this method will be triggered every time there is new FCM Message.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "전달받은 푸시: " + remoteMessage.from)

        if(remoteMessage.notification != null) {
            Log.d(TAG, "Notification Message Title: ${remoteMessage.notification?.title}")
            Log.d(TAG, "Notification Message Body: ${remoteMessage.notification?.body}")
            sendNotification(remoteMessage.notification?.title,remoteMessage.notification?.body)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(title:String?, body: String?) {
        // 클릭시 보낼 화면 정의 - jhm 2022/11/03
        // 현재 MainActivity 로 이동처리함 - jhm 2022/11/03
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            putExtra("Notification", title)
            putExtra("Notification", body)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        val notificationID = Random.nextInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        // Kotlin
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }else {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }


        val notification = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setFullScreenIntent(pendingIntent,true)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 우선순위 - jhm 2022/11/03
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.app_icon)
            .setAutoCancel(true) // 알림 클릭시 알림 제거 여부 - jhm 2022/11/03
            .setContentIntent(pendingIntent) // 클릭시 pendinIntent의 Activity로 이동 - jhm 2022/11/03
            .build()

        notificationManager.notify(notificationID, notification) // background notificaion action - jhm 2022/11/03
    }


    /**
     * Notification 진동
     * IMPORTANCE_HIGH : 알림음이 울리며 헤드업 알림
     * IMPORTANCE_DEFAULT : 알림음이 울립니다.
     * IMPORTANCE_LOW : 알림음이 없습니다.
     * IMPORTANCE_MIN : 알림음이 없고 상태표시줄에 표시되지 않습니다.
     * */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, IMPORTANCE_HIGH).apply {
            description = "Description"
            enableLights(true)
            lightColor = Color.GREEN
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

}

object Constants {
    const val CHANNEL_ID = "96071973609"
    const val CHANNEL_NAME = "piece_noti"
}