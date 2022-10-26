package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : MemberDTO
 * author         : piecejhm
 * date           : 2022/09/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/03        piecejhm       최초 생성
 */
public class MemberDTO {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("memberId")
        @Expose
        private String memberId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("pinNumber")
        @Expose
        private Object pinNumber;
        @SerializedName("cellPhoneNo")
        @Expose
        private String cellPhoneNo;
        @SerializedName("cellPhoneIdNo")
        @Expose
        private String cellPhoneIdNo;
        @SerializedName("birthDay")
        @Expose
        private String birthDay;
        @SerializedName("zipCode")
        @Expose
        private Object zipCode;
        @SerializedName("baseAddress")
        @Expose
        private Object baseAddress;
        @SerializedName("detailAddress")
        @Expose
        private Object detailAddress;
        @SerializedName("idNo")
        @Expose
        private Object idNo;
        @SerializedName("ci")
        @Expose
        private String ci;
        @SerializedName("di")
        @Expose
        private String di;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("email")
        @Expose
        private Object email;
        @SerializedName("isFido")
        @Expose
        private String isFido;
        @SerializedName("isIdNo")
        @Expose
        private String isIdNo;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("joinDay")
        @Expose
        private String joinDay;

        @SerializedName("totalProfitAmount")
        @Expose
        private String totalProfitAmount;

        @SerializedName("notification")
        @Expose
        private Notification notification;
        @SerializedName("device")
        @Expose
        private Device device;
        @SerializedName("consents")
        @Expose
        private List<Consent> consents = null;
        @SerializedName("bookmarks")
        @Expose
        private List<Bookmark> bookmarks = null;
        @SerializedName("portfolioNotifications")
        @Expose
        private List<Object> portfolioNotifications = null;

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getPinNumber() {
            return pinNumber;
        }

        public void setPinNumber(Object pinNumber) {
            this.pinNumber = pinNumber;
        }

        public String getCellPhoneNo() {
            return cellPhoneNo;
        }

        public void setCellPhoneNo(String cellPhoneNo) {
            this.cellPhoneNo = cellPhoneNo;
        }

        public String getCellPhoneIdNo() {
            return cellPhoneIdNo;
        }

        public void setCellPhoneIdNo(String cellPhoneIdNo) {
            this.cellPhoneIdNo = cellPhoneIdNo;
        }

        public String getBirthDay() {
            return birthDay;
        }

        public void setBirthDay(String birthDay) {
            this.birthDay = birthDay;
        }

        public Object getZipCode() {
            return zipCode;
        }

        public void setZipCode(Object zipCode) {
            this.zipCode = zipCode;
        }

        public Object getBaseAddress() {
            return baseAddress;
        }

        public void setBaseAddress(Object baseAddress) {
            this.baseAddress = baseAddress;
        }

        public Object getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(Object detailAddress) {
            this.detailAddress = detailAddress;
        }

        public Object getIdNo() {
            return idNo;
        }

        public void setIdNo(Object idNo) {
            this.idNo = idNo;
        }

        public String getCi() {
            return ci;
        }

        public void setCi(String ci) {
            this.ci = ci;
        }

        public String getDi() {
            return di;
        }

        public void setDi(String di) {
            this.di = di;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public String getIsFido() {
            return isFido;
        }

        public void setIsFido(String isFido) {
            this.isFido = isFido;
        }

        public String getIsIdNo() {
            return isIdNo;
        }

        public void setIsIdNo(String isIdNo) {
            this.isIdNo = isIdNo;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getJoinDay() {
            return joinDay;
        }

        public void setJoinDay(String joinDay) {
            this.joinDay = joinDay;
        }

        public String getTotalProfitAmount() {
            return totalProfitAmount;
        }

        public void setTotalProfitAmount(String totalProfitAmount) {
            this.totalProfitAmount = totalProfitAmount;
        }

        public Notification getNotification() {
            return notification;
        }

        public void setNotification(Notification notification) {
            this.notification = notification;
        }

        public Device getDevice() {
            return device;
        }

        public void setDevice(Device device) {
            this.device = device;
        }

        public List<Consent> getConsents() {
            return consents;
        }

        public void setConsents(List<Consent> consents) {
            this.consents = consents;
        }

        public List<Bookmark> getBookmarks() {
            return bookmarks;
        }

        public void setBookmarks(List<Bookmark> bookmarks) {
            this.bookmarks = bookmarks;
        }

        public List<Object> getPortfolioNotifications() {
            return portfolioNotifications;
        }

        public void setPortfolioNotifications(List<Object> portfolioNotifications) {
            this.portfolioNotifications = portfolioNotifications;
        }


        public class Notification {

            @SerializedName("memberId")
            @Expose
            private String memberId;
            @SerializedName("assetNotification")
            @Expose
            private String assetNotification;
            @SerializedName("portfolioNotification")
            @Expose
            private String portfolioNotification;
            @SerializedName("marketingSms")
            @Expose
            private String marketingSms;
            @SerializedName("marketingApp")
            @Expose
            private String marketingApp;

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

        public class Device {

            @SerializedName("memberId")
            @Expose
            private String memberId;
            @SerializedName("deviceId")
            @Expose
            private String deviceId;
            @SerializedName("deviceOs")
            @Expose
            private String deviceOs;
            @SerializedName("deviceState")
            @Expose
            private String deviceState;
            @SerializedName("fcmToken")
            @Expose
            private String fcmToken;
            @SerializedName("fbToken")
            @Expose
            private String fbToken;

            public String getMemberId() {
                return memberId;
            }

            public void setMemberId(String memberId) {
                this.memberId = memberId;
            }

            public String getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public String getDeviceOs() {
                return deviceOs;
            }

            public void setDeviceOs(String deviceOs) {
                this.deviceOs = deviceOs;
            }

            public String getDeviceState() {
                return deviceState;
            }

            public void setDeviceState(String deviceState) {
                this.deviceState = deviceState;
            }

            public String getFcmToken() {
                return fcmToken;
            }

            public void setFcmToken(String fcmToken) {
                this.fcmToken = fcmToken;
            }

            public String getFbToken() {
                return fbToken;
            }

            public void setFbToken(String fbToken) {
                this.fbToken = fbToken;
            }

        }



        public class Consent {

            @SerializedName("memberId")
            @Expose
            private String memberId;
            @SerializedName("consentCode")
            @Expose
            private String consentCode;
            @SerializedName("isAgreement")
            @Expose
            private String isAgreement;

            public String getMemberId() {
                return memberId;
            }

            public void setMemberId(String memberId) {
                this.memberId = memberId;
            }

            public String getConsentCode() {
                return consentCode;
            }

            public void setConsentCode(String consentCode) {
                this.consentCode = consentCode;
            }

            public String getIsAgreement() {
                return isAgreement;
            }

            public void setIsAgreement(String isAgreement) {
                this.isAgreement = isAgreement;
            }

        }

        public class Bookmark {

            @SerializedName("memberId")
            @Expose
            private String memberId;
            @SerializedName("magazineId")
            @Expose
            private String magazineId;
            @SerializedName("magazineType")
            @Expose
            private String magazineType;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("midTitle")
            @Expose
            private String midTitle;
            @SerializedName("smallTitle")
            @Expose
            private String smallTitle;
            @SerializedName("author")
            @Expose
            private String author;
            @SerializedName("representThumbnailPath")
            @Expose
            private String representThumbnailPath;
            @SerializedName("representImagePath")
            @Expose
            private String representImagePath;
            @SerializedName("contents")
            @Expose
            private String contents;
            @SerializedName("isDelete")
            @Expose
            private String isDelete;
            @SerializedName("createdAt")
            @Expose
            private String createdAt;
            @SerializedName("isFavorite")
            @Expose
            private String isFavorite;

            public String getMemberId() {
                return memberId;
            }

            public void setMemberId(String memberId) {
                this.memberId = memberId;
            }

            public String getMagazineId() {
                return magazineId;
            }

            public void setMagazineId(String magazineId) {
                this.magazineId = magazineId;
            }

            public String getMagazineType() {
                return magazineType;
            }

            public void setMagazineType(String magazineType) {
                this.magazineType = magazineType;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getMidTitle() {
                return midTitle;
            }

            public void setMidTitle(String midTitle) {
                this.midTitle = midTitle;
            }

            public String getSmallTitle() {
                return smallTitle;
            }

            public void setSmallTitle(String smallTitle) {
                this.smallTitle = smallTitle;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getRepresentThumbnailPath() {
                return representThumbnailPath;
            }

            public void setRepresentThumbnailPath(String representThumbnailPath) {
                this.representThumbnailPath = representThumbnailPath;
            }

            public String getRepresentImagePath() {
                return representImagePath;
            }

            public void setRepresentImagePath(String representImagePath) {
                this.representImagePath = representImagePath;
            }

            public String getContents() {
                return contents;
            }

            public void setContents(String contents) {
                this.contents = contents;
            }

            public String getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(String isDelete) {
                this.isDelete = isDelete;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getIsFavorite() {
                return isFavorite;
            }

            public void setIsFavorite(String isFavorite) {
                this.isFavorite = isFavorite;
            }

        }
    }

}
