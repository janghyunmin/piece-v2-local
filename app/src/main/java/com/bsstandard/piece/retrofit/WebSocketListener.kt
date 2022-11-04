package com.bsstandard.piece.retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bsstandard.piece.widget.utils.LogUtil
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

/**
 *packageName    : com.bsstandard.piece.retrofit
 * fileName       : WebSocketListener
 * author         : piecejhm
 * date           : 2022/10/14
 * description    : 웹소켓
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/14        piecejhm       최초 생성
 */


/**
 * 포트폴리오 조회 웹소켓 데이터 리스트
 * **/
data class Portfolio(
    val purchaseTotalAmount: Int,
    val portfolioId: String,
    val purchasePieceVolume: Int,
    val recruitmentState: String
)

/**
 * 포트폴리오 상세 웹소켓 데이터 리스트
 * **/
data class PortfolioDetail(
    val purchaseTotalAmount: Int
)

class WebSocketListener : WebSocketListener() {

    private val _liveData = MutableLiveData<JsonObject>()
    val liveData: LiveData<JsonObject> get() = _liveData

    private fun outputData(string: String) {
        val resp: JsonObject = JsonParser().parse(string).asJsonObject
        var jsonObject = JsonObject()
        jsonObject.add("data", resp)

        val post1Object: JsonObject? = jsonObject.getAsJsonObject("data")
        println(post1Object.toString())

        _liveData.postValue(jsonObject)
    }


//    private val _portfolioWsList = MutableLiveData<ArrayList<Portfolio>>()
//    val portfolioWsList: LiveData<ArrayList<Portfolio>> get() = _portfolioWsList
//
//    private val portfolioSocketData: ArrayList<Portfolio> = arrayListOf()
//
//
//    private fun outputData(string: String) {
//        val data = JSONObject(string)
//        LogUtil.logE("string : $string")
//
//        val jsonArray = data.optJSONArray("data")
//        var i = 0
//        var tempStr = ""
//
//        portfolioSocketData.clear()
//
//        while(i < jsonArray.length()) {
//            val jsonObject = jsonArray.getJSONObject(i)
//            portfolioSocketData.add(
//                Portfolio(
//                    jsonObject.getInt("purchaseTotalAmount"),
//                    jsonObject.getString("portfolioId"),
//                    jsonObject.getInt("purchasePieceVolume"),
//                    jsonObject.getString("recruitmentState")
//            ))
//        }
//
//
//        LogUtil.logE("portfolioId 0 : " + portfolioSocketData[0].portfolioId)
//        LogUtil.logE("portfolioId 1 : " + portfolioSocketData[1].portfolioId)
//
//        LogUtil.logE("purchaseTotalAmount 0 : " + portfolioSocketData[0].purchaseTotalAmount)
//        LogUtil.logE("purchaseTotalAmount 1 : " + portfolioSocketData[1].purchaseTotalAmount)
//
//        _portfolioWsList.value = string
//
//    }


    override fun onOpen(webSocket: WebSocket, response: Response?) {
        webSocket.send("")
        //webSocket.close(NORMAL_CLOSURE_STATUS, "Socket Close!!") //없을 경우 끊임없이 서버와 통신함
    }

    override fun onMessage(webSocket: WebSocket?, text: String) {
//        LogUtil.logE("Socket Receiving : $text")
        outputData(text)
    }

    override fun onMessage(webSocket: WebSocket?, bytes: ByteString) {
        LogUtil.logE("Socket Receiving bytes : $bytes")
        outputData(bytes.hex())
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        LogUtil.logE("Socket Closing : $code / $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        outputData("$code / $reason")
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable, response: Response?) {
        LogUtil.logE("Socket Error : " + t.message)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}