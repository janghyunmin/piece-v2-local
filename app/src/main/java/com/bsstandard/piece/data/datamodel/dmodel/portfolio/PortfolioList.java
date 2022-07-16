package com.bsstandard.piece.data.datamodel.dmodel.portfolio;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.portfolio
 * fileName       : PortfolioList
 * author         : piecejhm
 * date           : 2022/07/15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/15        piecejhm       최초 생성
 */
public class PortfolioList implements Parcelable {
    private String portfolioId;
    private String expectationProfitRate;
    private String representThumbnailImagePath;
    private String recruitmentState;
    private String remainingPieceVolume;
    private String recruitmentBeginDate;
    private String soldoutAt;
    private String createdAt;
    private String shareUrl;

    public PortfolioList(String portfolioId, String expectationProfitRate, String representThumbnailImagePath, String recruitmentState, String remainingPieceVolume, String recruitmentBeginDate,
                         String soldoutAt, String createdAt,String shareUrl) {
        this.portfolioId = portfolioId;
        this.expectationProfitRate = expectationProfitRate;
        this.representThumbnailImagePath = representThumbnailImagePath;
        this.recruitmentState = recruitmentState;
        this.remainingPieceVolume = remainingPieceVolume;
        this.recruitmentBeginDate = recruitmentBeginDate;
        this.soldoutAt = soldoutAt;
        this.createdAt = createdAt;
        this.shareUrl = shareUrl;
    }

    public PortfolioList(Parcel src) {
        portfolioId = src.readString();
        expectationProfitRate = src.readString();
        representThumbnailImagePath = src.readString();
        recruitmentState = src.readString();
        remainingPieceVolume = src.readString();
        recruitmentBeginDate = src.readString();
        soldoutAt = src.readString();
        createdAt = src.readString();
        shareUrl = src.readString();
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public PortfolioList createFromParcel(Parcel source) {
            return new PortfolioList(source);
        }

        @Override
        public PortfolioList[] newArray(int size) {
            return new PortfolioList[size];
        }
    };

    @Override
    public int describeContents() { return 0;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(portfolioId);
        dest.writeString(expectationProfitRate);
        dest.writeString(representThumbnailImagePath);
        dest.writeString(recruitmentState);
        dest.writeString(remainingPieceVolume);
        dest.writeString(recruitmentBeginDate);
        dest.writeString(soldoutAt);
        dest.writeString(createdAt);
        dest.writeString(shareUrl);
    }

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
}
