package com.bsstandard.piece.firebase

/**
 *packageName    : com.bsstandard.piece.firebase
 * fileName       : NotificationType
 * author         : piecejhm
 * date           : 2022/07/01
 * description    : Notification Type
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/01        piecejhm       최초 생성
 */

enum class NotificationType(val title: String, val id: Int) {
    NORMAL("일반 알림", 0),
    EXPANDABLE("확장형 알림", 1),
    CUSTOM("커스텀 알림", 3),
}