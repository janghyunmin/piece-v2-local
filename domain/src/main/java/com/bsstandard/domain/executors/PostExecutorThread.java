package com.bsstandard.domain.executors;

import io.reactivex.Scheduler;

/**
 * packageName    : com.bsstandard.domain.executors
 * fileName       : PostExecutorThread
 * author         : piecejhm
 * date           : 2022/04/29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/29        piecejhm       최초 생성
 */


public interface PostExecutorThread {
    Scheduler getScheduler();
}
