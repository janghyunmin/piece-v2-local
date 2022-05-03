package com.bsstandard.piece.widget.utils;

import com.bsstandard.domain.executors.PostExecutorThread;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * packageName    : com.bsstandard.piece.utils
 * fileName       : UIExecutorThread
 * author         : piecejhm
 * date           : 2022/04/29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/29        piecejhm       최초 생성
 */


@Singleton
public class UIExecutorThread implements PostExecutorThread {

    @Inject
    public UIExecutorThread() {
    }

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}