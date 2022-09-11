package com.bsstandard.piece.data.datasource.shared;

import android.content.Context;
import android.content.SharedPreferences;

import com.bsstandard.piece.data.dto.MemberDTO;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.datasource.shared
 * fileName       : PrefsHelper
 * author         : piecejhm
 * date           : 2022/07/03
 * description    : SharedPreference Common
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/03        piecejhm       최초 생성
 */

public class PrefsHelper {
    public static final String PREFERENCE_NAME = "PIECE";
    private Context mContext;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor prefsEditor;
    private static PrefsHelper instance;

    public static synchronized PrefsHelper init(Context context){
        if(instance == null)
            instance = new PrefsHelper(context);
        return instance;
    }

    private PrefsHelper(Context context){
        mContext = context;
        prefs = mContext.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
        prefsEditor = prefs.edit();
    }

    // String Shared - jhm 2022/07/04
    public static String read(String key, String defValue){
        return prefs.getString(key,defValue);
    }
    public static void write(String key,String value){
        prefsEditor.putString(key,value);
        prefsEditor.commit();
    }
    public static void removeKey(Context context,String key){
        prefsEditor.remove(key);
        prefsEditor.commit();
    }

    public static void removeToken(String key){
        prefsEditor.remove(key);
        prefsEditor.commit();
    }

    // Integer Shared - jhm 2022/07/04
    public static Integer read(String key,int defValue){
        return prefs.getInt(key,defValue);
    }
    public static void write(String key, Integer value){
        prefsEditor.putInt(key,value).commit();
    }



    // boolean Shared - jhm 2022/07/04
    public static boolean read(String key, boolean defValue){
        return prefs.getBoolean(key,defValue);
    }
    public static void write(String key,boolean value){
        prefsEditor.putBoolean(key,value);
        prefsEditor.commit();
    }

    // arrayList put - jhm 2022/09/05
    public static void writeList(String key , List<MemberDTO.Data.Consent> values) {
        JSONArray jsonArray = new JSONArray();
        for( int index = 0; index < values.size(); index++ ) {
            jsonArray.put(values.get(index));
        }
        if(!values.isEmpty()) {
            prefsEditor.putString(key,jsonArray.toString());
        } else {
            prefsEditor.putString(key, null);
        }
        prefsEditor.commit();
    }

    // arrayList get - jhm 2022/09/05
    public static ArrayList<String> getStringArrayPref(Context context, String key) {
        String json = prefs.getString(key,null);
        ArrayList<String> datas = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String data = jsonArray.optString(i);
                    datas.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return datas;
    }
}

