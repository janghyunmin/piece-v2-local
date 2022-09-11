package com.bsstandard.piece.data.datamodel.dmodel.magazine;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.magazine
 * fileName       : MemberBookmarkRemoveModel
 * author         : piecejhm
 * date           : 2022/08/29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/29        piecejhm       최초 생성
 */
public class MemberBookmarkRemoveModel {
    private String memberId;
    private String magazineId;

    public MemberBookmarkRemoveModel(String memberId, String magazineId) {
        this.memberId = memberId;
        this.magazineId = magazineId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMagazineId() {
        return magazineId;
    }

    public void setMagazineId(String magazineId) {
        this.magazineId = magazineId;
    }
}
