package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * packageName    : com.bsstandard.piece.data.vo
 * fileName       : JoinVO
 * author         : piecejhm
 * date           : 2022/06/30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/30        piecejhm       최초 생성
 */

public class JoinDTO {
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

    public JoinDTO withStatus(String status) {
        this.status = status;
        return this;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public JoinDTO withMessage(Object message) {
        this.message = message;
        return this;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public JoinDTO withData(Data data) {
        this.data = data;
        return this;
    }

    public class Data {

        @SerializedName("memberId")
        @Expose
        private String memberId;
        @SerializedName("deviceId")
        @Expose
        private String deviceId;
        @SerializedName("accessToken")
        @Expose
        private String accessToken;
        @SerializedName("refreshToken")
        @Expose
        private String refreshToken;
        @SerializedName("expiredAt")
        @Expose
        private String expiredAt;

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public Data withMemberId(String memberId) {
            this.memberId = memberId;
            return this;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public Data withDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public Data withAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public Data withRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public String getExpiredAt() {
            return expiredAt;
        }

        public void setExpiredAt(String expiredAt) {
            this.expiredAt = expiredAt;
        }

        public Data withExpiredAt(String expiredAt) {
            this.expiredAt = expiredAt;
            return this;
        }

    }
}