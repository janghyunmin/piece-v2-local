package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bsstandard.piece.data.dto.EventDetailDTO
import com.bsstandard.piece.data.repository.EventDetailRepository
import com.bsstandard.piece.widget.utils.LogUtil
import kotlinx.coroutines.launch

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : EventDetailViewModel
 * author         : piecejhm
 * date           : 2022/09/11
 * description    : 이벤트 상세 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/11        piecejhm       최초 생성
 */

class EventDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: EventDetailRepository = EventDetailRepository(application)
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    val detailResponse: MutableLiveData<EventDetailDTO> = MutableLiveData()
    @SuppressLint("CheckResult")
    fun getEventDetail(eventId: String) {
        // 뷰모델이 사라지면 코루틴도 같이 삭제 - jhm 2022/09/11
        viewModelScope.launch {
            val response = repo.getEventDetails(eventId = eventId)
            try {
                if(response.status.equals("OK")) {
                    detailResponse.value = response
                } else {
                    detailResponse.value = response
                    LogUtil.logE("이벤트 상세 try Error !")
                }
            } catch (ex : Exception) {
                LogUtil.logE("이벤트 상세 try catch Error !")
                ex.printStackTrace()
            }
        }
    }
}