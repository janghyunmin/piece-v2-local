package com.bsstandard.piece.widget.utils

/**
 *packageName    : com.bsstandard.piece.widget.utils
 * fileName       : CustomDialogListener
 * author         : piecejhm
 * date           : 2022/07/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/04        piecejhm       최초 생성
 */

interface CustomDialogListener {
    fun onOkButtonClicked() // 버전 업데이트 확인 버튼 Listener
}
interface CustomDialogPassCodeListener {
    fun onCancleButtonClicked() // 비밀번호 재설정 버튼 취소 Listener
    fun onRetryPassCodeButtonClicked() // 비밀번호 재설정 버튼 Listener
}