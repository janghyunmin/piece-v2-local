package com.bsstandard.piece.firebase;

import android.content.Context;
import android.util.Log;

import com.bsstandard.piece.data.datasource.shared.PrefsHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * packageName    : com.bsstandard.piece.firebase
 * fileName       : MyFirebaseMessagingService
 * author         : piecejhm
 * date           : 2022/07/06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/06        piecejhm       최초 생성
 */


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        PrefsHelper.init(getApplicationContext());
        PrefsHelper.write("fb",s);
//        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    public static String getToken(Context context) {
        return PrefsHelper.read("fb","empty");
//                context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }
}
