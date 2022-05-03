package com.bsstandard.data.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * packageName    : com.bsstandard.data.utils
 * fileName       : SharedPreferenceUtil
 * author         : piecejhm
 * date           : 2022/05/02
 * description    : SharedPreferenceUtil
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/02        piecejhm       최초 생성
 */


public class SharedPreferenceUtil {
    public static final String PREFERENCE ="gosuApp";

    private static SharedPreferenceUtil sharedPreferenceModule = null;
    private static Context mContext;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static SharedPreferenceUtil getInstance(Context context) {
        mContext = context;

        if(sharedPreferenceModule == null) {
            sharedPreferenceModule = new SharedPreferenceUtil();
        }

        if(sharedPreferences == null) {
            sharedPreferences = mContext.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return sharedPreferenceModule;
    }
    public void putPermission(String key,String chk){
        editor.putString( key,chk );
        editor.apply();
    }
    public String getString(String key){
        return sharedPreferences.getString( key,null );
    }

    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }
}