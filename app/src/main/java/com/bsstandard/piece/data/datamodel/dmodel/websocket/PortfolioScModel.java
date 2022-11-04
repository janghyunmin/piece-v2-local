package com.bsstandard.piece.data.datamodel.dmodel.websocket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.websocket
 * fileName       : PortfolioScModel
 * author         : piecejhm
 * date           : 2022/11/04
 * description    : 포트폴리오 조회 웹소켓 모델
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/11/04        piecejhm       최초 생성
 */


public class PortfolioScModel {
    @SerializedName("purchaseTotalAmount")
    @Expose
    private int purchaseTotalAmount;

    @SerializedName("portfolioId")
    @Expose
    private String portfolioId;

    @SerializedName("purchasePieceVolume")
    @Expose
    private int purchasePieceVolume;

    @SerializedName("recruitmentState")
    @Expose
    private String recruitmentState;

    public PortfolioScModel(int purchaseTotalAmount, String portfolioId, int purchasePieceVolume, String recruitmentState) {
        this.purchaseTotalAmount = purchaseTotalAmount;
        this.portfolioId = portfolioId;
        this.purchasePieceVolume = purchasePieceVolume;
        this.recruitmentState = recruitmentState;
    }

    public int getPurchaseTotalAmount() {
        return purchaseTotalAmount;
    }

    public void setPurchaseTotalAmount(int purchaseTotalAmount) {
        this.purchaseTotalAmount = purchaseTotalAmount;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public int getPurchasePieceVolume() {
        return purchasePieceVolume;
    }

    public void setPurchasePieceVolume(int purchasePieceVolume) {
        this.purchasePieceVolume = purchasePieceVolume;
    }

    public String getRecruitmentState() {
        return recruitmentState;
    }

    public void setRecruitmentState(String recruitmentState) {
        this.recruitmentState = recruitmentState;
    }
}
