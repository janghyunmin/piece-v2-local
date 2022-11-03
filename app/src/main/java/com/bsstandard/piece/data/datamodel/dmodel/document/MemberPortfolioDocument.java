package com.bsstandard.piece.data.datamodel.dmodel.document;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.document
 * fileName       : MemberPortfolioDocument
 * author         : piecejhm
 * date           : 2022/10/31
 * description    : 소유증서 신청 Model
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/31        piecejhm       최초 생성
 */


public class MemberPortfolioDocument {
    private String memberId;
    private String purchaseId;
    private String sendDvn;

    public MemberPortfolioDocument(String memberId, String purchaseId,String sendDvn) {
        this.memberId = memberId;
        this.purchaseId = purchaseId;
        this.sendDvn = sendDvn;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getSendDvn() {
        return sendDvn;
    }

    public void setSendDvn(String sendDvn) {
        this.sendDvn = sendDvn;
    }
}
