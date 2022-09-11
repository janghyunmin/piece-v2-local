package com.bsstandard.piece.data.viewmodel

import androidx.lifecycle.ViewModel
import com.bsstandard.piece.widget.utils.SingleLiveEvent

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : MoreViewModel
 * author         : piecejhm
 * date           : 2022/09/01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/01        piecejhm       최초 생성
 */
class MoreViewModel() : ViewModel(){
//    private val _viewEvent = MutableLiveData<Event<Any>>()
//    val viewEvent: LiveData<Event<Any>>
//        get() = _viewEvent
//
//    fun viewEvent(content: Any) {
//        _viewEvent.value = Event(content)
//    }


    val startMyInfoDetail = SingleLiveEvent<Any>()
    val startNotice = SingleLiveEvent<Any>()
    val startEvent = SingleLiveEvent<Any>()

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
}