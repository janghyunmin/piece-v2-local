package com.bsstandard.piece.widget.utils;

import android.util.Log;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : LogUtil
 * author         : piecejhm
 * date           : 2022/05/02
 * description    : 로그 처리시 공통유틸
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/02        piecejhm       최초 생성
 */


public class LogUtil {
    public static void logE(String msg) {
        StackTraceElement trace = Thread.currentThread().getStackTrace()[3];
        String fileName = trace.getFileName();
        String classPath = trace.getClassName();
        String className = classPath.substring(classPath.lastIndexOf(".") + 1);
        String methodName = trace.getMethodName();
        int lineNumber = trace.getLineNumber();

        Log.e("APP# " + className + "." + methodName + "(" + fileName + ":" + lineNumber + ")", msg);
    }
}