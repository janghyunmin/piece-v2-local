package com.bsstandard.piece.widget.utils

import androidx.lifecycle.MutableLiveData

/**
 *packageName    : com.bsstandard.piece.utils
 * fileName       : NotNullMutableLiveData
 * author         : piecejhm
 * date           : 2022/04/27
 * description    : LiveData !! getValue() null 처리
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/27        piecejhm       최초 생성
 */

class NotNullMutableLiveData<E : Any>(
    defaultValue: E
) : MutableLiveData<E>(defaultValue) {
    override fun getValue(): E {
        return super.getValue()!!
    }
}