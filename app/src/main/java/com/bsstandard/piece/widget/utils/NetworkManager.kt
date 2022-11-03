package com.bsstandard.piece.widget.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.os.Build
import androidx.lifecycle.LiveData
import java.util.*
import kotlin.concurrent.schedule

/**
 *packageName    : com.bsstandard.piece.utils
 * fileName       : NetworkManager
 * author         : piecejhm
 * date           : 2022/04/27
 * description    : 네트워크 체크 모듈
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/27        piecejhm       최초 생성
 */


// 네트워크 연결 확인을 위해 네트워크 변경 시 알람에 사용하는 클래스 NetworkCallback 을 커스터마이징
class NetworkConnection(private val context: Context) : LiveData<Boolean>()
{
    private var connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    override fun onActive()
    {
        super.onActive()
        updateConnection()

        when
        {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ->
            {
                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback())
            }
            else ->
            {
                context.registerReceiver(
                    networkReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }
        }
    }

    override fun onInactive()
    {
        super.onInactive()
        try {
            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback())
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        finally {

        }
    }

    private fun connectivityManagerCallback(): ConnectivityManager.NetworkCallback
    {
        networkCallback = object : ConnectivityManager.NetworkCallback()
        {
            override fun onLost(network: Network)
            {
                super.onLost(network)
                postValue(false)
            }

            override fun onAvailable(network: Network)
            {
                super.onAvailable(network)
                postValue(true)
            }
        }
        return networkCallback
    }

    private val networkReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context?, intent: Intent?)
        {
            updateConnection()
        }
    }

    private fun updateConnection()
    {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected == true)
    }

}