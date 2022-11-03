package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bsstandard.piece.data.dto.VersionDTO
import com.bsstandard.piece.data.repository.AppVersionRepository
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : AppVersionViewModel
 * author         : piecejhm
 * date           : 2022/11/03
 * description    : AppVersion ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/11/03        piecejhm       최초 생성
 */

@HiltViewModel
class AppVersionViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: AppVersionRepository = AppVersionRepository(application)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    val detailResponse: MutableLiveData<VersionDTO> = MutableLiveData()
    private val _version: MutableLiveData<String> = MutableLiveData()
    val version: LiveData<String>
    get() = _version

    @SuppressLint("CheckResult")
    fun getAppVersion(deviceType: String) {
        viewModelScope.launch {
            val response = repo.getAppVersion(deviceType = deviceType)
            try {
                if(response.status.equals("OK")) {
                    detailResponse.value = response
                    _version.value = response.data.version
                    _version.postValue(response.data.version)
                }
            } catch (ex: Exception) {
                LogUtil.logE("앱 버전 조회 Catch : ${ex.message}")
                ex.printStackTrace()
            }
        }
    }


}