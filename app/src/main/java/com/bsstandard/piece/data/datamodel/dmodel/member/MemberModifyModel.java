package com.bsstandard.piece.data.datamodel.dmodel.member;

import com.bsstandard.piece.data.datamodel.dmodel.consent.UpdateConsentList;

import java.util.ArrayList;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.member
 * fileName       : MemberModifyModel
 * author         : piecejhm
 * date           : 2022/09/05
 * description    : 회원 정보 수정
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/05        piecejhm       최초 생성
 */
public class MemberModifyModel {
    private String memberId;
    private String name;
    private String pinNumber;
    private String cellPhoneNo;
    private String cellPhoneIdNo;
    private String birthDay;
    private String zipCode;
    private String baseAddress;
    private String detailAddress;
    private String ci;
    private String di;
    private String gender;
    private String email;
    private String isFido;
    private NotificationModel notification;
    private ArrayList<UpdateConsentList> consents;

    public class Notification {
        private String memberId;
        private String assetNotification;
        private String portfolioNotification;
        private String marketingSms;
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

    public class Consents {
        private String memberId;
        private String consentCode;
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

    public MemberModifyModel(String memberId, String name, String pinNumber, String cellPhoneNo, String cellPhoneIdNo,
                             String birthDay, String zipCode, String baseAddress, String detailAddress, String ci,
                             String di, String gender, String email, String isFido, NotificationModel notification, ArrayList<UpdateConsentList> consents) {
        this.memberId = memberId;
        this.name = name;
        this.pinNumber = pinNumber;
        this.cellPhoneNo = cellPhoneNo;
        this.cellPhoneIdNo = cellPhoneIdNo;
        this.birthDay = birthDay;
        this.zipCode = zipCode;
        this.baseAddress = baseAddress;
        this.detailAddress = detailAddress;
        this.ci = ci;
        this.di = di;
        this.gender = gender;
        this.email = email;
        this.isFido = isFido;
        this.notification = notification;
        this.consents = consents;
    }

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

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getBaseAddress() {
        return baseAddress;
    }

    public void setBaseAddress(String baseAddress) {
        this.baseAddress = baseAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsFido() {
        return isFido;
    }

    public void setIsFido(String isFido) {
        this.isFido = isFido;
    }

    public NotificationModel getNotification() {
        return notification;
    }

    public void setNotification(NotificationModel notification) {
        this.notification = notification;
    }

    public ArrayList<UpdateConsentList> getConsents() {
        return consents;
    }

    public void setConsents(ArrayList<UpdateConsentList> consents) {
        this.consents = consents;
    }
}


