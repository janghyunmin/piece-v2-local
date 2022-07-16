package com.bsstandard.piece.data.datamodel.dmodel.join;

import java.util.ArrayList;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel
 * fileName       : JoinModel
 * author         : piecejhm
 * date           : 2022/06/30
 * description    : 회원가입시 필요 모델
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/30        piecejhm       최초 생성
 */

public class JoinModel {
    private String name;
    private String pinNumber;
    private String cellPhoneNo;
    private String birthDay;
    private String ci;
    private String di;
    private String gender;
    private String isFido;
    private DeviceInfoModel deviceInfo = null;
    private NotificationInfoModel notificationInfo = null;
    private ArrayList<ConsentModel> consents = null;

    public JoinModel(String name, String pinNumber, String cellPhoneNo, String birthDay, String ci, String di, String gender,
                     String isFido, DeviceInfoModel deviceInfo, NotificationInfoModel notificationInfo, ArrayList<ConsentModel> consents){
        this.name = name;
        this.pinNumber = pinNumber;
        this.cellPhoneNo = cellPhoneNo;
        this.birthDay = birthDay;
        this.ci = ci;
        this.di = di;
        this.gender = gender;
        this.isFido = isFido;
        this.deviceInfo = deviceInfo;
        this.notificationInfo = notificationInfo;
        this.consents = consents;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JoinModel withName(String name) {
        this.name = name;
        return this;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    public JoinModel withPinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
        return this;
    }

    public String getCellPhoneNo() {
        return cellPhoneNo;
    }

    public void setCellPhoneNo(String cellPhoneNo) {
        this.cellPhoneNo = cellPhoneNo;
    }

    public JoinModel withCellPhoneNo(String cellPhoneNo) {
        this.cellPhoneNo = cellPhoneNo;
        return this;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public JoinModel withBirthDay(String birthDay) {
        this.birthDay = birthDay;
        return this;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public JoinModel withCi(String ci) {
        this.ci = ci;
        return this;
    }

    public String getDi() {
        return di;
    }

    public void setDi(String di) {
        this.di = di;
    }

    public JoinModel withDi(String di) {
        this.di = di;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public JoinModel withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getIsFido() {
        return isFido;
    }

    public void setIsFido(String isFido) {
        this.isFido = isFido;
    }

    public JoinModel withIsFido(String isFido) {
        this.isFido = isFido;
        return this;
    }


    public DeviceInfoModel getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfoModel deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public NotificationInfoModel getNotificationInfo() {
        return notificationInfo;
    }

    public void setNotificationInfo(NotificationInfoModel notificationInfo) {
        this.notificationInfo = notificationInfo;
    }

    public ArrayList<ConsentModel> getConsents() {
        return consents;
    }

    public void setConsents(ArrayList<ConsentModel> consents) {
        this.consents = consents;
    }
}
