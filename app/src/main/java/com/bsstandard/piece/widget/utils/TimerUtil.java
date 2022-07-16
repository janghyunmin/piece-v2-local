package com.bsstandard.piece.widget.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : TimerUtil
 * author         : piecejhm
 * date           : 2022/06/23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/23        piecejhm       최초 생성
 */
public class TimerUtil {

    int count_minute;// 분
    int count_second;// 초
    TextView tv_count;// 숫자 보여줄 textView id
    int second;//최초의 초 를 가지고있기위한변수(반복사용때문에)
    Boolean isStarting = false;// 카운트 진행중인지 체크
    Timer mTimer = new Timer();

    public TimerUtil(int count_minute, int count_second, TextView tv_count) {
        this.count_minute = count_minute;
        this.count_second = count_second;
        this.tv_count = tv_count;

        this.second = count_second;
    }

    /*
    카운터 시작
    */
    public void startTimer() {
        isStarting = true;

        // schdule 함수 인자별 사용법

        // schdule(task 세팅값 , (몇초 후에 task 를 실행하는것을 => 세트)몇초 마다 세트를 계속 실행해라 , 몇초 후에 task 실행해라)

        // schdule(task 세팅값 , (한번만)몇초 후에 task 실행해라)

        mTimer.schedule(initTimeTask(), 0, 1000);
    }

    /*
    카운터 종료
    */
    public void cancelTimer() {
        isStarting = false;
        mTimer.cancel();
        tv_count.setText("00:00");
    }

    /*
    카운터 진행중 체크
    */
    public Boolean isStarting() {
        return isStarting;
    }

    /*
    휴대폰인증 유효시간 타이머
    제어가능부분 :  textview숫자표시 , 분 , 초
    실행 : ex) 02:59  > 02:58 ... > 00:00
    */
    public TimerTask initTimeTask() {
        TimerTask mTask = new TimerTask() {

            @Override
            public void run() {
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = mHandler.obtainMessage();
                        mHandler.sendMessage(msg);

                        count_second--;

                        if (count_second < 0) {
                            count_second = second - 1;//디지털시간에서 60 표시는 없기때문임
                            count_minute--;
                        }
                        if (count_minute == 0 && count_second == 0) {
                            cancelTimer();
                        }
                        NumberFormat format = new DecimalFormat("%02d");

                        String str_second = Integer.toString(count_second);
                        String str_minute = Integer.toString(count_minute);
                        LogUtil.logE("second : " + Integer.toString(count_second));
                        LogUtil.logE("minute : " + Integer.toString(count_minute));

                        tv_count.setText(str_minute + ":" + str_second);

                    }
                }, 0);
            }
        };
        return mTask;
    }
}
