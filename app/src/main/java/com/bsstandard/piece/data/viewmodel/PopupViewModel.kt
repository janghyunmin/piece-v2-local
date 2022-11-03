package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bsstandard.piece.data.dto.PopupDTO
import com.bsstandard.piece.data.repository.PopupRepository
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : PopupViewModel
 * author         : piecejhm
 * date           : 2022/10/29
 * description    : 팝업 조회 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/29        piecejhm       최초 생성
 */

@HiltViewModel
class PopupViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: PopupRepository = PopupRepository(application)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    val popupResponse: MutableLiveData<PopupDTO> = MutableLiveData()
    @SuppressLint("CheckResult")
    fun getPopup(popupType: String) {
        repo.getPopup(popupType = popupType).subscribe(
            {
                try {
                    LogUtil.logE("팝업 조회 Response Code : ${it.statusCode}")
                    if(it.status.equals("OK")) {
                        LogUtil.logE("팝업 조회 성공 !")
                        popupResponse.value = it
                    } else {
                        LogUtil.logE("팝업 조회 실패 !" + it.statusCode)
                        popupResponse.value = it
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    LogUtil.logE("팝업 조회 Catch Error !.." + ex.message)
                }
                LogUtil.logE("팝업 조회")
            }, { throwable ->
                LogUtil.logE("팝업 조회 GET Error ! " + throwable.message)
            }
        )
    }
}