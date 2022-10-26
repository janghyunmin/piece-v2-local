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
    // 탈퇴 불가 (카카오톡 문의하기) 버튼 - jhm 2022/09/20
    // 등록된 계좌가 없어요 - 계좌 등록 버튼 - jhm 2022/10/03
    // 계좌 확인 실패 - jhm 2022/10/05
    fun onOkButtonClicked()

    // 쿠폰 취소하기 버튼 - jhm 2022/09/13
    // 탈퇴 불가 취소 버튼 - jhm 2022/09/21
    // 등록된 계좌가 없어요 뒤로 버튼 - jhm 2022/10/03
    fun onCancelButtonClicked()

}
interface CustomDialogPassCodeListener {
    fun onCancleButtonClicked() // 비밀번호 재설정 버튼 취소 Listener
    fun onRetryPassCodeButtonClicked() // 비밀번호 재설정 버튼 Listener
}

// 증빙자료 이미지 닫기 Listener - jhm 2022/10/13
interface ImageCloseListener {
    fun onClickCancelButton()
}

interface CustomCouponListener {
    // 쿠폰 사용하기 버튼 - jhm 2022/09/13
    fun onOkBtnClicked()
}

