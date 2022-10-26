package com.bsstandard.piece.data.datamodel.dmodel.purchase;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.purchase
 * fileName       : PurchaseModel
 * author         : piecejhm
 * date           : 2022/10/19
 * description    : 포트폴리오 구매시 필요 Model
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/19        piecejhm       최초 생성
 */


public class PurchaseModel {
    private String portfolioId;
    private Integer purchaseVolume;

    public PurchaseModel(String portfolioId, Integer purchaseVolume) {
        this.portfolioId = portfolioId;
        this.purchaseVolume = purchaseVolume;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public Integer getPurchaseVolume() {
        return purchaseVolume;
    }

    public void setPurchaseVolume(Integer purchaseVolume) {
        this.purchaseVolume = purchaseVolume;
    }
}
