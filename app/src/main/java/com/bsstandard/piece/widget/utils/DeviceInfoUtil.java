package com.bsstandard.piece.widget.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.util.UUID;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : DeviceInfoUtil
 * author         : piecejhm
 * date           : 2022/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/03        piecejhm       최초 생성
 */


public class DeviceInfoUtil {
    /**
     * uuid 가져오기
     * @param context
     * @return
     */
    public static String getUUID(Context context) {
        return UUID.randomUUID().toString();
    }


    /**
     * device id 가져오기
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * device 제조사 가져오기
     * @return
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * device 브랜드 가져오기
     * @return
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * device 모델명 가져오기
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * device Android OS 버전 가져오기
     * @return
     */
    public static String getDeviceOs() {
        return Build.VERSION.RELEASE;
    }

    /**
     * device SDK 버전 가져오기
     * @return
     */
    public static int getDeviceSdk() {
        return Build.VERSION.SDK_INT;
    }
}
