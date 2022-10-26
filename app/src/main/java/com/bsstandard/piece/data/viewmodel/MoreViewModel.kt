package com.bsstandard.piece.data.viewmodel

import androidx.lifecycle.ViewModel
import com.bsstandard.piece.widget.utils.SingleLiveEvent

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : MoreViewModel
 * author         : piecejhm
 * date           : 2022/09/01
 * description    : 더보기 OnClick Event ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/01        piecejhm       최초 생성
 */
class MoreViewModel() : ViewModel(){
    val startMyInfoDetail = SingleLiveEvent<Any>()
    val startNotice = SingleLiveEvent<Any>()
    val startEvent = SingleLiveEvent<Any>()
    val startCoupon = SingleLiveEvent<Any>()
    val startAccess = SingleLiveEvent<Any>()
    val startNotiSetting = SingleLiveEvent<Any>()
    val startConsent = SingleLiveEvent<Any>()
    val startQuestion = SingleLiveEvent<Any>()

    // 내정보 상세 - jhm 2022/09/10
    fun myInfo() {
        startMyInfoDetail.call()
    }

    // 공지사항 - jhm 2022/09/10
    fun goNotice() {
        startNotice.call()
    }

    // 이벤트 - jhm 2022/09/11
    fun goEvent() {
        startEvent.call()
    }

    // 쿠폰함 - jhm 2022/09/13
    fun goCoupon() {
        startCoupon.call()
    }

    // 인증 및 보안 - jhm 2022/09/15
    fun goAccess() {
        startAccess.call()
    }

    // 알림 설정 - jhm 2022/09/21
    fun goNotiSetting() {
        startNotiSetting.call()
    }

    // 약관 및 개인정보 처리 등 동의 - jhm 2022/09/22
    fun goConsent() {
        startConsent.call()
    }

    // 자주 묻는 질문 - jhm 2022/09/23
    fun goQuestion() {
        startQuestion.call()
    }
}