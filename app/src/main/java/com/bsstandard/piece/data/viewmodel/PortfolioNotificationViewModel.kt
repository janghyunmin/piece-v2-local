package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bsstandard.piece.data.dto.PortfolioNotiDTO
import com.bsstandard.piece.data.repository.PortfolioNotificationRepository
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : PortfolioNotificationViewModel
 * author         : piecejhm
 * date           : 2022/11/02
 * description    : 포트폴리오 알림 설정 여부 조회 요청 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/11/02        piecejhm       최초 생성
 */


@HiltViewModel
class PortfolioNotificationViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: PortfolioNotificationRepository = PortfolioNotificationRepository(application)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    val detailResponse: MutableLiveData<PortfolioNotiDTO> = MutableLiveData()
    private val _notificationYn: MutableLiveData<String> = MutableLiveData()
    val notificationYn: LiveData<String>
        get() = _notificationYn

    @SuppressLint("CheckResult")
    fun getPortfolioNotification(portfolioId: String) {
        viewModelScope.launch {
            val response = repo.getPortfolioNotification(portfolioId = portfolioId)
            try {
                if(response.status.equals("OK")) {
                    detailResponse.value = response
                    _notificationYn.value = response.data.notificationYn
                    _notificationYn.postValue(response.data.notificationYn)

                } else {
                    LogUtil.logE("포트폴리오 알림 여부 조회 try Error !")
                }
            } catch (ex: Exception) {
                LogUtil.logE("포트폴리오 알림 여부 조회 Catch ${ex.message}")
                ex.printStackTrace()
            }
        }
    }
}