package com.bsstandard.piece.widget.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : ErrorController
 * author         : piecejhm
 * date           : 2022/05/02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/02        piecejhm       최초 생성
 */


public class ErrorController {

    public static final String TAG = "ERROR";

    private static boolean DEBUG = true;

    /**
     * 단순히 Log.e 메세지를 로그에 출력해주는 래퍼.
     *
     * @param msg 내용
     */
    public static void showMessage(String msg) {

        if (DEBUG) {

            Log.e(TAG, msg);
        }
    }


    /**
     * Log.e(TAG, "MSG", e); 를 간략화한 래퍼.
     *
     * @param e catch문에서 던져지는 Exception.
     */
    public static void showError(Exception e) {
        Log.e(TAG, "Exception.", e);
    }

    /**
     * Log.e(TAG, "MSG", e); 를 간략화한 래퍼.
     *
     * @param t catch문에서 던져지는 Exception.
     */
    public static void showError(Throwable t) {
        Log.e(TAG, "Exception.", t);
    }

    /**
     * "[Debug] To be implemented" 라는 토스트를 보여준다. 아직 미구현인 이벤트 콜백에 쓰도록 한다.
     *
     * @param context 해당 화면의 context.
     */
    private static void showToBeImplementedToast(Context context) {
        try {
            Toast.makeText(context, "[Debug] To be implemented.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            showError(e);
        }
    }

    /**
     * Toast.makeText(context, msg, LENGTH).show()를 간략화한 래퍼
     *
     * @param context 해당 화면의 context.
     * @param msg     내용
     */
    public static void showToast(Context context, String msg) {
        try {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            showError(e);
        }
    }

    /**
     * Toast.makeText(context, "[DEBUG] " + msg, LENGTH).show()를 간략화한 래퍼.
     * 디버그 토스트를 표현할 떄 사용한다.
     *
     * @param context 해당 화면의 context.
     * @param msg     내용
     */
    public static void showDebugToast(Context context, String msg) {
        try {
            Toast.makeText(context, "[Debug] " + msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            showError(e);
        }
    }


}