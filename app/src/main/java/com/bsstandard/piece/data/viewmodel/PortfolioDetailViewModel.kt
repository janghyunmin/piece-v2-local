package com.bsstandard.piece.data.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : PortfolioDetailViewModel
 * author         : piecejhm
 * date           : 2022/07/18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/18        piecejhm       최초 생성
 */


@HiltViewModel
class PortfolioDetailViewModel(application: Application) : AndroidViewModel(application) {
    val main_text: ObservableField<String> = ObservableField("포트폴리오 상세")
    val mApplication = application


    fun onClickButton(){
        // TODO: Click 시 Room에 데이터를 추가해야 함.
        Toast.makeText(mApplication,"Click!", Toast.LENGTH_SHORT).show()
    }
}
