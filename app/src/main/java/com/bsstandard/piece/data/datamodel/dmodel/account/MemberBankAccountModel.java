package com.bsstandard.piece.data.datamodel.dmodel.account;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.account
 * fileName       : MemberBankAccountModel
 * author         : piecejhm
 * date           : 2022/10/04
 * description    : 회원 출금 계좌 등록(변경) 요청
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/04        piecejhm       최초 생성
 */


public class MemberBankAccountModel {
    private String accountBankCode;
    private String accountNo;

    public MemberBankAccountModel(String accountBankCode, String accountNo) {
        this.accountBankCode = accountBankCode;
        this.accountNo = accountNo;
    }

    public String getAccountBankCode() {
        return accountBankCode;
    }

    public void setAccountBankCode(String accountBankCode) {
        this.accountBankCode = accountBankCode;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
}
