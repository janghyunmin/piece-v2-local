package com.bsstandard.piece.data.datamodel.dmodel.occupation;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.occupation
 * fileName       : OccupationVerifyModel
 * author         : piecejhm
 * date           : 2022/10/06
 * description    : 가상계좌 인증번호 검증 모델
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/06        piecejhm       최초 생성
 */

public class OccupationVerifyModel {
    private String authNo;
    private String mchtTrdNo;

    public OccupationVerifyModel(String authNo, String mchtTrdNo) {
        this.authNo = authNo;
        this.mchtTrdNo = mchtTrdNo;
    }

    public String getAuthNo() {
        return authNo;
    }

    public void setAuthNo(String authNo) {
        this.authNo = authNo;
    }

    public String getMchtTrdNo() {
        return mchtTrdNo;
    }

    public void setMchtTrdNo(String mchtTrdNo) {
        this.mchtTrdNo = mchtTrdNo;
    }
}
