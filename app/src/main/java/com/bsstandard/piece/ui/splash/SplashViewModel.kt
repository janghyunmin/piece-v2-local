package com.bsstandard.piece.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bsstandard.domain.usecase.GetLoginUseCase

/**
 *packageName    : com.bsstandard.piece.ui.splash
 * fileName       : SplashViewModel
 * author         : piecejhm
 * date           : 2022/04/28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/28        piecejhm       최초 생성
 */


class SplashViewModel(private val getLoginUseCase: GetLoginUseCase) : ViewModel() {
    private val _goLogin: MutableLiveData<Unit> = MutableLiveData()

    val goLogin: LiveData<Unit> get() = _goLogin

    fun goSplash() {
        if(getLoginUseCase.execute()) {      // 자동 로그인 가능 여부
            _goLogin.value = Unit
        } else {
            _goLogin.value = Unit
        }
    }



}