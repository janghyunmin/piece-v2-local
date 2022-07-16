package com.bsstandard.piece.data.datamodel.dmodel;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel
 * fileName       : MemberPinModel
 * author         : piecejhm
 * date           : 2022/07/05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/05        piecejhm       최초 생성
 */
public class MemberPinModel {
    String memberId;
    String pinNumber;

    public MemberPinModel(String memberId, String pinNumber){
        this.memberId = memberId;
        this.pinNumber = pinNumber;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    @Override
    public String toString() {
        return "memberId : " + memberId + "\n" +
                "pinNumber : " + pinNumber;
    }
}
