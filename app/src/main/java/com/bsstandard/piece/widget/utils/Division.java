package com.bsstandard.piece.widget.utils;

import android.Manifest;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : Division
 * author         : piecejhm
 * date           : 2022/05/02
 * description    : 공통 전역 변수
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/02        piecejhm       최초 생성
 */


public class Division {

    /** API URL **/
    public static final String PIECE_API_V2_DEV = "https://lui1qyiqx4.apigw.ntruss.com/piece-dev/v2/";
    public static final String PIECE_API_V2_PROD = "https://z8danh2uj7.apigw.ntruss.com/pieve-prod/v2/";

    /** Permission Code **/
    public static final int PERMISSIONS_REQUEST_CODE = 100;
    public static final String[] REQUEST_PER = {
            Manifest.permission.INTERNET
            , Manifest.permission.USE_BIOMETRIC
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.USE_FINGERPRINT
            , Manifest.permission.ACCESS_WIFI_STATE
            , Manifest.permission.CHANGE_WIFI_STATE
            , Manifest.permission.ACCESS_NETWORK_STATE
    };


    public static final String PERMISSION_CHECK_OK = "Y";
    public static final String PERMISSION_CHECK_NO = "N";



    /** ViewType 정의 - jhm 2022/05/02 **/
    public static final String DIALOG_C_VERSION = "version"; // 버전 체크 Dialog
    public static final String DIALOG_C_PASSWORD = "password"; // 비밀번호 재설정 Dialog
    public static final String DIALOG_J_PHONE = "phone_select"; // 통신사 선택 Dialog
    public static final String DIALOG_J_TERMS = "terms_select"; // 약관 동의 Dialog
    public static final String DIALOG_J_SMS = "sms"; // SMS 문자발송 Dialog
    public static final String PIN_VIEWTYPE_1 = "최초";
    public static final String PIN_VIEWTYPE_2 = "재인증";
    public static final String DIALOG_ADDRESS_CONFIRM = "address"; // 주소변경 완료 후 Dialog - jhm 2022/09/07









}