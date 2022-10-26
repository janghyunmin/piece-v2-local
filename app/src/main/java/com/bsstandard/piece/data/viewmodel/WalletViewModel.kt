package com.bsstandard.piece.data.viewmodel

import androidx.lifecycle.ViewModel
import com.bsstandard.piece.widget.utils.SingleLiveEvent

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : WalletViewModel
 * author         : piecejhm
 * date           : 2022/09/28
 * description    : 내지갑 OnClick Event ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/28        piecejhm       최초 생성
 */


class WalletViewModel : ViewModel() {
    val startDeposit = SingleLiveEvent<Any>()

    // 나의 예치금 잔액 - jhm 2022/09/28
    fun myDeposit() {
        startDeposit.call()
    }
}