package com.bsstandard.piece.widget.utils;

import android.util.Log;

import com.bsstandard.piece.widget.extension.CryptLib;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : EncryptUtil
 * author         : piecejhm
 * date           : 2022/07/05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/05        piecejhm       최초 생성
 */

public class EncryptUtil {

    private static EncryptUtil encryptUtil = null;

    public static EncryptUtil getInstance() {
        if(encryptUtil == null) {
            encryptUtil = new EncryptUtil();
        }
        return encryptUtil;
    }

    public String EncryptToDecrypt(String decryptText, String time){

        Log.e("바디값", "값 : "+ decryptText);

        String preKey = reverseString(time.substring(2,10));
        String postKey = "%^^&&@(*&@##)@)@``~~" + preKey;

        CryptLib cryptLib = null;
        try {
            cryptLib = new CryptLib();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        String encryptText = null;
        try {
            encryptText = cryptLib.encryptPlainTextWithRandomIV(decryptText, postKey, true);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return encryptText;
    }

    public static String reverseString(String s) {
        return ( new StringBuffer(s) ).reverse().toString();
    }
}
