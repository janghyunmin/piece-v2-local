package com.bsstandard.piece.data.datamodel.dmodel.member;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.member
 * fileName       : Notification
 * author         : piecejhm
 * date           : 2022/09/21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/21        piecejhm       최초 생성
 */
public class NotificationModel {
    private String memberId;
    private String assetNotification;
    private String portfolioNotification;
    private String marketingSms;
    private String marketingApp;

    public NotificationModel(String memberId, String assetNotification, String portfolioNotification, String marketingSms, String marketingApp) {
        this.memberId = memberId;
        this.assetNotification = assetNotification;
        this.portfolioNotification = portfolioNotification;
        this.marketingSms = marketingSms;
        this.marketingApp = marketingApp;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getAssetNotification() {
        return assetNotification;
    }

    public void setAssetNotification(String assetNotification) {
        this.assetNotification = assetNotification;
    }

    public String getPortfolioNotification() {
        return portfolioNotification;
    }

    public void setPortfolioNotification(String portfolioNotification) {
        this.portfolioNotification = portfolioNotification;
    }

    public String getMarketingSms() {
        return marketingSms;
    }

    public void setMarketingSms(String marketingSms) {
        this.marketingSms = marketingSms;
    }

    public String getMarketingApp() {
        return marketingApp;
    }

    public void setMarketingApp(String marketingApp) {
        this.marketingApp = marketingApp;
    }
}
