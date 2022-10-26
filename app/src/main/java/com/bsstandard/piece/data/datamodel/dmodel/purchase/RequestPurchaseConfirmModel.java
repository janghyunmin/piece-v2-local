package com.bsstandard.piece.data.datamodel.dmodel.purchase;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.purchase
 * fileName       : RequestPurchaseConfirmModel
 * author         : piecejhm
 * date           : 2022/10/25
 * description    : 포트폴리오 구매 확정 요청
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/25        piecejhm       최초 생성
 */


public class RequestPurchaseConfirmModel {
    private String purchaseId;
    private String portfolioId;
    private String isAgree;

    public RequestPurchaseConfirmModel(String purchaseId, String portfolioId, String isAgree) {
        this.purchaseId = purchaseId;
        this.portfolioId = portfolioId;
        this.isAgree = isAgree;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(String isAgree) {
        this.isAgree = isAgree;
    }
}
