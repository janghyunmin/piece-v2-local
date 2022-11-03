package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bsstandard.piece.data.dto.MemberMagazineDetailDTO
import com.bsstandard.piece.data.repository.MemberMagazineDetailRepository
import com.bsstandard.piece.widget.utils.LogUtil
import kotlinx.coroutines.launch

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : MemberMagazineDetailViewModel
 * author         : piecejhm
 * date           : 2022/11/01
 * description    : 회원 매거진 상세 조회 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/11/01        piecejhm       최초 생성
 */

class MemberMagazineDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: MemberMagazineDetailRepository = MemberMagazineDetailRepository(application)
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    val detailResponse: MutableLiveData<MemberMagazineDetailDTO> = MutableLiveData()

    private val _isFavorite: MutableLiveData<String> = MutableLiveData()
    val isFavorite: LiveData<String>
        get() = _isFavorite


    @SuppressLint("CheckResult")
    fun getMemberMagazineDetail(magazineId: String) {
        // 뷰모델이 사라지면 코루틴도 같이 삭제 - jhm 2022/09/11
        viewModelScope.launch {
            val response = repo.getMemberMagazineDetail(magazineId = magazineId)
            try {
                if(response.status.equals("OK")) {
                    detailResponse.value = response
                    LogUtil.logE("isFavorite : ${response.data.isFavorite}")
                    _isFavorite.value = response.data.isFavorite
                    _isFavorite.postValue(response.data.isFavorite)
                } else {
                    detailResponse.value = response
                    LogUtil.logE("회원 매거진 상세 try Error !")
                }
            }catch (ex : Exception) {
                LogUtil.logE("회원 매거진 상세 try Error !")
                ex.printStackTrace()
            }
        }
    }
}