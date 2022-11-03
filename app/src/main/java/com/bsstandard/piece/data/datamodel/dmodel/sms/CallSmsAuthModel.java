package com.bsstandard.piece.data.datamodel.dmodel.sms;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel
 * fileName       : CallSmsAuthModel
 * author         : piecejhm
 * date           : 2022/06/22
 * description    : 본인 인증 sms 요청 Model
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/22        piecejhm       최초 생성
 */
public class CallSmsAuthModel {
    private String name;
    private String birthday;
    private String sexCd;
    private String ntvFrnrCd;
    private String telComCd;
    private String telNo;
    private String agree1;
    private String agree2;
    private String agree3;
    private String agree4;


    public CallSmsAuthModel(String name, String birthday, String sexCd, String ntvFrnrCd,
                            String telComCd, String telNo, String agree1, String agree2, String agree3, String agree4) {
        this.name = name;
        this.birthday = birthday;
        this.sexCd = sexCd;
        this.ntvFrnrCd = ntvFrnrCd;
        this.telComCd = telComCd;
        this.telNo = telNo;
        this.agree1 = agree1;
        this.agree2 = agree2;
        this.agree3 = agree3;
        this.agree4 = agree4;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSexCd() {
        return sexCd;
    }

    public void setSexCd(String sexCd) {
        this.sexCd = sexCd;
    }

    public String getNtvFrnrCd() {
        return ntvFrnrCd;
    }

    public void setNtvFrnrCd(String ntvFrnrCd) {
        this.ntvFrnrCd = ntvFrnrCd;
    }

    public String getTelComCd() {
        return telComCd;
    }

    public void setTelComCd(String telComCd) {
        this.telComCd = telComCd;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
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

    public String getAgree3() {
        return agree3;
    }

    public void setAgree3(String agree3) {
        this.agree3 = agree3;
    }

    public String getAgree4() {
        return agree4;
    }

    public void setAgree4(String agree4) {
        this.agree4 = agree4;
    }
}
