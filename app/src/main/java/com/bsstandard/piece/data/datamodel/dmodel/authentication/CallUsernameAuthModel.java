package com.bsstandard.piece.data.datamodel.dmodel.authentication;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.authentication
 * fileName       : CallUsernameAuthModel
 * author         : piecejhm
 * date           : 2022/10/26
 * description    : 실명인증 Model
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/26        piecejhm       최초 생성
 */


public class CallUsernameAuthModel {
    private String memberId;
    private String name;
    private String ssn;
    private String agree1;
    private String agree2;

    public CallUsernameAuthModel(String memberId, String name, String ssn, String agree1, String agree2) {
        this.memberId = memberId;
        this.name = name;
        this.ssn = ssn;
        this.agree1 = agree1;
        this.agree2= agree2;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getAgree1() {
        return agree1;
    }

    public void setAgree1(String agree1) {
        this.agree1 = agree1;
    }

    public String getAgree2() {
        return agree2;
    }

    public void setAgree2(String agree2) {
        this.agree2 = agree2;
    }
}
