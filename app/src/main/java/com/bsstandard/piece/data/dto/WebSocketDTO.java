package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : WebSocketDTO
 * author         : piecejhm
 * date           : 2022/10/19
 * description    : 포트폴리오 웹소켓 DTO
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/19        piecejhm       최초 생성
 */
public class WebSocketDTO {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Datum {

        @SerializedName("purchaseTotalAmount")
        @Expose
        private Integer purchaseTotalAmount;
        @SerializedName("portfolioId")
        @Expose
        private String portfolioId;
        @SerializedName("purchasePieceVolume")
        @Expose
        private Integer purchasePieceVolume;
        @SerializedName("recruitmentState")
        @Expose
        private String recruitmentState;

        public Integer getPurchaseTotalAmount() {
            return purchaseTotalAmount;
        }

        public void setPurchaseTotalAmount(Integer purchaseTotalAmount) {
            this.purchaseTotalAmount = purchaseTotalAmount;
        }

        public String getPortfolioId() {
            return portfolioId;
        }

        public void setPortfolioId(String portfolioId) {
            this.portfolioId = portfolioId;
        }

        public Integer getPurchasePieceVolume() {
            return purchasePieceVolume;
        }

        public void setPurchasePieceVolume(Integer purchasePieceVolume) {
            this.purchasePieceVolume = purchasePieceVolume;
        }

        public String getRecruitmentState() {
            return recruitmentState;
        }

        public void setRecruitmentState(String recruitmentState) {
            this.recruitmentState = recruitmentState;
        }

    }
}
