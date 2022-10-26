package com.bsstandard.piece.widget.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 *packageName    : com.bsstandard.piece.widget.utils
 * fileName       : MyWorker
 * author         : piecejhm
 * date           : 2022/10/24
 * description    : 스케쥴러
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/24        piecejhm       최초 생성
 */


class MyWorker (appContext : Context, workerParams: WorkerParameters) : Worker(appContext,workerParams){
    override fun doWork(): Result {
        var count = 0
        var doCounting = true
        while (doCounting){
            LogUtil.logE("Test Worker ${++count}")
            if(count == 60){
                doCounting = false
            }
            Thread.sleep(1000)
        }
        return Result.success()
    }

}
