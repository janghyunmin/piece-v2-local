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

class WebData {
    var purchaseTotalAmount: Int? = null
    var portfolioId: String? = null
    var purchasePieceVolume: Int? = null
    var recruitmentState: String? = null

    override fun toString(): String {
        return "purchaseTotalAmount $purchaseTotalAmount, portfolioId $portfolioId ," +
                "purchasePieceVolume $purchasePieceVolume , recruitmentState $recruitmentState"
    }
}
class WebSocketListener : WebSocketListener() {

    private val _liveData = MutableLiveData<JsonObject>()
    val liveData: LiveData<JsonObject> get() = _liveData

    var listdata = ArrayList<String>()
    private fun outputData(string: String) {
        LogUtil.logE("string : $string" )

        val resp: JsonObject = JsonParser().parse(string).asJsonObject
        var jsonObject = JsonObject()
        jsonObject.add("data", resp)

        _liveData.postValue(jsonObject)
    }


    override fun onOpen(webSocket: WebSocket, response: Response?) {
        webSocket.send("")
        webSocket.close(NORMAL_CLOSURE_STATUS, "Socket Close!!") //없을 경우 끊임없이 서버와 통신함
    }

    override fun onMessage(webSocket: WebSocket?, text: String) {
        LogUtil.logE("Socket Receiving : $text")
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