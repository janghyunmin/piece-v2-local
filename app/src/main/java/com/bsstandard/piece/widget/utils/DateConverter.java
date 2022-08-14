package com.bsstandard.piece.widget.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : DateConverter
 * author         : piecejhm
 * date           : 2022/07/18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/18        piecejhm       최초 생성
 */


public class DateConverter {

    public static Date getDate(String from) throws ParseException {
        // "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
        return transFormat.parse(from);
    }

    public static String getString(Date date, String pattern) {
        SimpleDateFormat transFormat = new SimpleDateFormat(pattern, Locale.KOREA);
        return transFormat.format(date);
    }

    public static String resultDateToString(String date, String pattern) throws ParseException {
        Date tempDate = getDate(date);
        return getString(tempDate, pattern);
    }

    public static Long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static Long getStringToTime(String date) throws ParseException {
        return getDate(date).getTime();
    }

    public static String getUploadMinuteTime(Long curTime, String dateTime, String pattern) throws ParseException {

        long boardTime = getStringToTime(dateTime);
        long result = ((curTime - boardTime) / 60000) * -1;

        if (result < 60) {
            return result + "분만에 마감!";
        }
        else if (result == 60) {
            result = result / 60;
            return result + "시간만에 마감!";
        }
        else if(result <= 1440){ // 24시간을 분으로 바꿔 1440 - jhm 2022/07/18
            long hour = result / 60;
            long minute = result % 60;
            return hour + "시간" + minute + "분만에 마감!";
        }
        else {
            long day = result / 1440;
            return day + "일만에 마감!";
        }
    }
}
