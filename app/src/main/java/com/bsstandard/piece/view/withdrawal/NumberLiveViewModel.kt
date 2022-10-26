package com.bsstandard.piece.view.withdrawal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 *packageName    : com.bsstandard.piece.view.withdrawal
 * fileName       : NumberLiveViewModel
 * author         : piecejhm
 * date           : 2022/10/04
 * description    : 출금 금액 입력 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/04        piecejhm       최초 생성
 */

@HiltViewModel
class NumberLiveViewModel : ViewModel() {
    val textValue1 : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val textValue2 : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val textValue3 : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val textValue4 : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val textValue5 : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val textValue6 : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val textValue7 : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val textValue8 : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val textValue9 : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val textValue0 : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val textAmount : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun returnText(): MutableLiveData<String> {
        return textAmount;
    }

}