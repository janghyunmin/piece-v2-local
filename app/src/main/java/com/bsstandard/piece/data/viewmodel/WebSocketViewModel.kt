package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.bsstandard.piece.widget.utils.LogUtil
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : MainViewModel
 * author         : piecejhm
 * date           : 2022/10/14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/14        piecejhm       최초 생성
 */


class WebSocketViewModel : ViewModel() {
    // val url = "http://example.com:8080/"
    val url = "ws://192.168.0.39:10000/portfolio" // 소켓에 연결하는 엔드포인트가 /portfolio 일때 다음과 같음
    val stompClient =  Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

    @SuppressLint("CheckResult")
    fun runStomp(){
//        stompClient.topic("ws://192.168.0.39:10000/portfolio").subscribe {
//                topicMessage ->
//            LogUtil.logE("message Recieve" + topicMessage.payload)
//        }

        val headerList = arrayListOf<StompHeader>()
//        headerList.add(StompHeader("positionType", "1"))
        stompClient.connect(headerList)

        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    LogUtil.logE("OPEND !!")
                }
                LifecycleEvent.Type.CLOSED -> {
                    LogUtil.logE("CLOSED !!")

                }
                LifecycleEvent.Type.ERROR -> {
                    LogUtil.logE("ERROR !!")
                    LogUtil.logE("CONNECT ERROR  " + lifecycleEvent.exception.toString())
                }
                else ->{
                    LogUtil.logE("ELSE" + lifecycleEvent.message)
                }
            }
        }

        val data = JSONObject()
        data.put("userKey", "")


        // portfolioId - jhm 2022/10/14
        stompClient.send("", data.toString()).subscribe()
    }
}