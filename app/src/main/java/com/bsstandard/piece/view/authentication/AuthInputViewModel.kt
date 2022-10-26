package com.bsstandard.piece.view.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bsstandard.piece.widget.utils.LogUtil

/**
 *packageName    : com.bsstandard.piece.view.authentication
 * fileName       : AuthInputViewModel
 * author         : piecejhm
 * date           : 2022/10/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/26        piecejhm       최초 생성
 */

class AuthInputViewModel(application: Application) :
    AndroidViewModel(application) {

    var userFirst = MutableLiveData<String>()
    fun getFirst(): MutableLiveData<String> {
        return userFirst
    }

    var userLast = MutableLiveData<String>()
    fun getLast(): MutableLiveData<String> {
        return userLast
    }

    // 사용자 입력 값 주민번호 - jhm 2022/09/13
    open fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        LogUtil.logE("onTextChanged $s")
    }

}