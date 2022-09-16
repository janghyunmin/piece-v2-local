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
    // 버전 업데이트 확인 버튼 Listener - jhm 2022/08/05
    // 주소 등록 완료 확인 버튼 - jhm 2022/09/07
    // 쿠폰 사용하기 버튼 - jhm 2022/09/13
    fun onOkButtonClicked()

    // 쿠폰 취소하기 버튼 - jhm 2022/09/13
    fun onCancelButtonClicked()

}
interface CustomDialogPassCodeListener {
    fun onCancleButtonClicked() // 비밀번호 재설정 버튼 취소 Listener
    fun onRetryPassCodeButtonClicked() // 비밀번호 재설정 버튼 Listener
}


interface CustomCouponListener {
    // 쿠폰 사용하기 버튼 - jhm 2022/09/13
    fun onOkBtnClicked()
}

