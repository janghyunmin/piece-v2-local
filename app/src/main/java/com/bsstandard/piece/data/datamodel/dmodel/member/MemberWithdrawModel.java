package com.bsstandard.piece.data.datamodel.dmodel.member;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.member
 * fileName       : MemberWithdrawModel
 * author         : piecejhm
 * date           : 2022/09/19
 * description    : 회원 탈퇴 전용 Data Model
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/19        piecejhm       최초 생성
 */


public class MemberWithdrawModel {
    private String withdrawalReasonCode;
    private String withdrawalReasonText;

    public MemberWithdrawModel(String withdrawalReasonCode, String withdrawalReasonText) {
        this.withdrawalReasonCode = withdrawalReasonCode;
        this.withdrawalReasonText = withdrawalReasonText;
    }

    public String getWithdrawalReasonCode() {
        return withdrawalReasonCode;
    }

    public void setWithdrawalReasonCode(String withdrawalReasonCode) {
        this.withdrawalReasonCode = withdrawalReasonCode;
    }

    public String getWithdrawalReasonText() {
        return withdrawalReasonText;
    }

    public void setWithdrawalReasonText(String withdrawalReasonText) {
        this.withdrawalReasonText = withdrawalReasonText;
    }
}
