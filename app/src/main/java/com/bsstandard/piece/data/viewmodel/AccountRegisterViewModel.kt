package com.bsstandard.piece.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bsstandard.piece.widget.utils.LogUtil

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : AccountRegisterViewModel
 * author         : piecejhm
 * date           : 2022/10/05
 * description    : 출금계좌 등록시 필요 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/05        piecejhm       최초 생성
 */


class AccountRegisterViewModel(application: Application) : AndroidViewModel(application) {

    var accountNo = MutableLiveData<String>()
    fun getAccountNum(): MutableLiveData<String> {
        return accountNo
    }

    // 사용자 입력 값 계좌번호 - jhm 2022/09/13
    open fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        LogUtil.logE("tagonTextChanged $s")
    }
}