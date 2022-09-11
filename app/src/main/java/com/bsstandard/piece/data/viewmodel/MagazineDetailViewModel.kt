package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bsstandard.piece.data.dto.MagazineDetailDTO
import com.bsstandard.piece.data.repository.MagazineDetailRepository
import com.bsstandard.piece.widget.utils.LogUtil
import kotlinx.coroutines.launch

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : MagazineDetailViewModel
 * author         : piecejhm
 * date           : 2022/08/30
 * description    : 매거진(라운지) 상세 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/30        piecejhm       최초 생성
 */

class MagazineDetailViewModel(application: Application): AndroidViewModel(application) {
    private val repo: MagazineDetailRepository = MagazineDetailRepository(application)
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    val detailResponse: MutableLiveData<MagazineDetailDTO> = MutableLiveData()
    @SuppressLint("CheckResult")
    fun getMagazineDetail(magazineId: String) {
        // 뷰모델이 사라지면 코루틴도 같이 삭제 - jhm 2022/08/30
        viewModelScope.launch {
            val response = repo.getMagazineDetails(magazineId =  magazineId)
            try {
                if(response.status.equals("OK")) {
                    detailResponse.value = response
                } else {
                    detailResponse.value = response
                    LogUtil.logE("매거진 상세 try Error !")
                }
            }catch (ex : Exception) {
                LogUtil.logE("매거진 상세 try Error !")
                ex.printStackTrace()
            }
        }
    }
}