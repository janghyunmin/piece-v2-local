package com.bsstandard.piece.widget.utils;

import com.bsstandard.piece.widget.extension.CryptLib;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : DecryptUtil
 * author         : piecejhm
 * date           : 2022/07/05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/05        piecejhm       최초 생성
 */

public class DecryptUtil {

    private static DecryptUtil decryptUtil = null;

    public static DecryptUtil getInstance() {
        if(decryptUtil == null) {
            decryptUtil = new DecryptUtil();
        }
        return decryptUtil;
    }

    public String EncryptToDecrypt(String decryptText, String timestamp){

        String preKey =  reverseString(timestamp.substring(2, 10));
        String postKey = "%^^&&@(*&@##)@)@``~~" + preKey;

        CryptLib cryptLib = null;
        try {
            cryptLib = new CryptLib();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        String decryptedText = null;
        try {
            decryptedText = cryptLib.decryptCipherTextWithRandomIV(decryptText, postKey, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    public static String reverseString(String s) {
        return ( new StringBuffer(s) ).reverse().toString();
    }
}