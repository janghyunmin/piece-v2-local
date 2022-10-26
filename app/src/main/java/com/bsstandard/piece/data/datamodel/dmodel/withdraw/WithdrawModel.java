package com.bsstandard.piece.data.datamodel.dmodel.withdraw;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.withdraw
 * fileName       : WithdrawModel
 * author         : piecejhm
 * date           : 2022/10/04
 * description    : 회원 예치금 출금 신청 요청 모델
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/04        piecejhm       최초 생성
 */

public class WithdrawModel {
    private Integer withdrawRequestAmount;

    public WithdrawModel(Integer withdrawRequestAmount) {
        this.withdrawRequestAmount = withdrawRequestAmount;
    }

    public Integer getWithdrawRequestAmount() {
        return withdrawRequestAmount;
    }

    public void setWithdrawRequestAmount(Integer withdrawRequestAmount) {
        this.withdrawRequestAmount = withdrawRequestAmount;
    }
}
