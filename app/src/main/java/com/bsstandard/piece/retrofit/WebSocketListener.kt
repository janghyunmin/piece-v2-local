package com.bsstandard.piece.retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.widget.utils.LogUtil
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject

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


data class Portfolio(
    val data : ArrayList<String>,
    val purchaseTotalAmount: Int,
    val portfolioId: String,
    val purchasePieceVolume: Int,
    val recruitmentState: String
)
class WebSocketListener : WebSocketListener() {

    private val _liveData = MutableLiveData<JSONObject>()
    val liveData: LiveData<JSONObject> get() = _liveData

    var listdata = ArrayList<String>()


    private fun outputData(string: String) {
        val data = JSONObject(string)

        val jsonArray = data.optJSONArray("data")
        var i = 0
        var tempStr = ""
        while(i < jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val purchaseTotalAmount = jsonObject.getInt("purchaseTotalAmount")
            tempStr += "[purchaseTotalAmount : $purchaseTotalAmount], "
            i++
        }
        // 소켓데이터 Custom - jhm 2022/11/02
//        LogUtil.logE("tempStr $tempStr")

        val firstStatus = jsonArray.getJSONObject(0)
        val recruitmentState = firstStatus.getString("recruitmentState")
        PrefsHelper.write("recruitmentState",recruitmentState)

        _liveData.postValue(data)
    }


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