package com.bsstandard.piece.data.datamodel.dmodel.join;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.join
 * fileName       : NotificationInfo
 * author         : piecejhm
 * date           : 2022/06/30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/30        piecejhm       최초 생성
 */

public class NotificationInfoModel {
    private String assetNotification;
    private String portfolioNotification;
    private String marketingSms;
    private String marketingApp;

    public NotificationInfoModel(String assetNotification, String portfolioNotification, String marketingSms, String marketingApp){
        this.assetNotification = assetNotification;
        this.portfolioNotification = portfolioNotification;
        this.marketingSms = marketingSms;
        this.marketingApp = marketingApp;
    }

    public String getAssetNotification() {
        return assetNotification;
    }

    public void setAssetNotification(String assetNotification) {
        this.assetNotification = assetNotification;
    }

    public NotificationInfoModel withAssetNotification(String assetNotification) {
        this.assetNotification = assetNotification;
        return this;
    }

    public String getPortfolioNotification() {
        return portfolioNotification;
    }

    public void setPortfolioNotification(String portfolioNotification) {
        this.portfolioNotification = portfolioNotification;
    }

    public NotificationInfoModel withPortfolioNotification(String portfolioNotification) {
        this.portfolioNotification = portfolioNotification;
        return this;
    }

    public String getMarketingSms() {
        return marketingSms;
    }

    public void setMarketingSms(String marketingSms) {
        this.marketingSms = marketingSms;
    }

    public NotificationInfoModel withMarketingSms(String marketingSms) {
        this.marketingSms = marketingSms;
        return this;
    }

    public String getMarketingApp() {
        return marketingApp;
    }

    public void setMarketingApp(String marketingApp) {
        this.marketingApp = marketingApp;
    }

    public NotificationInfoModel withMarketingApp(String marketingApp) {
        this.marketingApp = marketingApp;
        return this;
    }

}
