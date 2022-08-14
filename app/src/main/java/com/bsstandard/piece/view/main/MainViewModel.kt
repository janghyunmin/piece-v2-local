package com.bsstandard.piece.view.main

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 *packageName    : com.bsstandard.piece.ui.main
 * fileName       : MainViewModel
 * author         : piecejhm
 * date           : 2022/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/03        piecejhm       최초 생성
 */

@HiltViewModel
class MainViewModel(application: Application) : AndroidViewModel(application) {
    val main_text: ObservableField<String> = ObservableField("포트폴리오")
    val mApplication = application


    fun onClickButton(){
        Toast.makeText(mApplication,"Click!",Toast.LENGTH_SHORT).show()
    }
}
