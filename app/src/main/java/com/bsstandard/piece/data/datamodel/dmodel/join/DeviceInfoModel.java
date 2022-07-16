package com.bsstandard.piece.data.datamodel.dmodel.join;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.join
 * fileName       : DeviceInfo
 * author         : piecejhm
 * date           : 2022/06/30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/30        piecejhm       최초 생성
 */
public class DeviceInfoModel {
    private String deviceId;
    private String deviceOs;
    private String deviceState;
    private String fcmToken;
    private String fbToken;

    public DeviceInfoModel(String deviceId, String deviceOs, String deviceState, String fcmToken, String fbToken){
        this.deviceId = deviceId;
        this.deviceOs = deviceOs;
        this.deviceState = deviceState;
        this.fcmToken = fcmToken;
        this.fbToken = fbToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceInfoModel withDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public DeviceInfoModel withDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
        return this;
    }

    public String getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(String deviceState) {
        this.deviceState = deviceState;
    }

    public DeviceInfoModel withDeviceState(String deviceState) {
        this.deviceState = deviceState;
        return this;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public DeviceInfoModel withFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        return this;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }

    public DeviceInfoModel withFbToken(String fbToken) {
        this.fbToken = fbToken;
        return this;
    }
}
