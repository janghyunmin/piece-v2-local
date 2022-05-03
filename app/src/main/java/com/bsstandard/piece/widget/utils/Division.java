package com.bsstandard.piece.widget.utils;

import android.Manifest;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : Division
 * author         : piecejhm
 * date           : 2022/05/02
 * description    : 분기 변수 공통
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/02        piecejhm       최초 생성
 */


public class Division {


    /** Permission Code **/
    public static final int PERMISSIONS_REQUEST_CODE = 100;
    public static final String[] REQUEST_PER = {
            Manifest.permission.INTERNET
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.ACCESS_WIFI_STATE
            , Manifest.permission.CHANGE_WIFI_STATE
            , Manifest.permission.ACCESS_NETWORK_STATE
    };


    public static final String PERMISSION_CHECK_OK = "Y";
    public static final String PERMISSION_CHECK_NO = "N";

    /**** Retrofit 분기 변수 처리***/
    public static final String LOCAL_BASE_URL = "https://20igr2325g.apigw.ntruss.com/piece-dev/v1/";


    /** ViewType 정의 - jhm 2022/05/02 **/
    public static final int VIEW_ITEM_0 = 0;
    public static final int VIEW_ITEM_1 = 1;





}