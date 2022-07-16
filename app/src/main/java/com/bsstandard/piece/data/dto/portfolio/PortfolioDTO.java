package com.bsstandard.piece.data.dto.portfolio;

import androidx.work.Data;

import com.bsstandard.piece.data.dto.BaseDTO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.dto.portfolio
 * fileName       : PortfolioDTo
 * author         : piecejhm
 * date           : 2022/07/14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/14        piecejhm       최초 생성
 */
public class PortfolioDTO {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("data")
        @Expose
        private List<Datum> data = null;
        @SerializedName("totalCount")
        @Expose
        private Integer totalCount;

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

        public class Datum {

            @SerializedName("portfolioId")
            @Expose
            private String portfolioId;
            @SerializedName("expectationProfitRate")
            @Expose
            private String expectationProfitRate;
            @SerializedName("representThumbnailImagePath")
            @Expose
            private String representThumbnailImagePath;
            @SerializedName("recruitmentState")
            @Expose
            private String recruitmentState;
            @SerializedName("remainingPieceVolume")
            @Expose
            private String remainingPieceVolume;
            @SerializedName("recruitmentBeginDate")
            @Expose
            private String recruitmentBeginDate;
            @SerializedName("soldoutAt")
            @Expose
            private String soldoutAt;
            @SerializedName("createdAt")
            @Expose
            private String createdAt;
            @SerializedName("shareUrl")
            @Expose
            private String shareUrl;

            public String getPortfolioId() {
                return portfolioId;
            }

            public void setPortfolioId(String portfolioId) {
                this.portfolioId = portfolioId;
            }

            public String getExpectationProfitRate() {
                return expectationProfitRate;
            }

            public void setExpectationProfitRate(String expectationProfitRate) {
                this.expectationProfitRate = expectationProfitRate;
            }

            public String getRepresentThumbnailImagePath() {
                return representThumbnailImagePath;
            }

            public void setRepresentThumbnailImagePath(String representThumbnailImagePath) {
                this.representThumbnailImagePath = representThumbnailImagePath;
            }

            public String getRecruitmentState() {
                return recruitmentState;
            }

            public void setRecruitmentState(String recruitmentState) {
                this.recruitmentState = recruitmentState;
            }

            public String getRemainingPieceVolume() {
                return remainingPieceVolume;
            }

            public void setRemainingPieceVolume(String remainingPieceVolume) {
                this.remainingPieceVolume = remainingPieceVolume;
            }

            public String getRecruitmentBeginDate() {
                return recruitmentBeginDate;
            }

            public void setRecruitmentBeginDate(String recruitmentBeginDate) {
                this.recruitmentBeginDate = recruitmentBeginDate;
            }

            public String getSoldoutAt() {
                return soldoutAt;
            }

            public void setSoldoutAt(String soldoutAt) {
                this.soldoutAt = soldoutAt;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getShareUrl() {
                return shareUrl;
            }

            public void setShareUrl(String shareUrl) {
                this.shareUrl = shareUrl;
            }


            @Override
            public String toString() {
                return "PortfolioList { " +
                        "portfolioId='" + portfolioId + '\'' +
                        ", expectationProfitRate='" + expectationProfitRate + '\'' +
                        ", representThumbnailImagePath='" + representThumbnailImagePath + '\'' +
                        ", recruitmentStat='" + recruitmentState + '\'' +
                        ", remainingPieceVolume='" + remainingPieceVolume + '\'' +
                        ", recruitmentBeginDate='" + recruitmentBeginDate + '\'' +
                        ", soldoutAt='" + soldoutAt + '\'' +
                        ", createdAt='" + createdAt + '\'' +
                        ", shareUrl='" + shareUrl + '\'' + '}';
            }
        }
    }
}
