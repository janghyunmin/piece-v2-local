package com.bsstandard.piece.data.datamodel.dmodel.purchase;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.purchase
 * fileName       : PurchaseRmModel
 * author         : piecejhm
 * date           : 2022/10/25
 * description    : 포트폴리오 구매 취소 요청 Model
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/25        piecejhm       최초 생성
 */


public class PurchaseRmModel {
    private String purchaseId;

    public PurchaseRmModel(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }
}
