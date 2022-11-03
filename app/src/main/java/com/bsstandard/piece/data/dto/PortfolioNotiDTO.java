package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : PortfolioNotiDTO
 * author         : piecejhm
 * date           : 2022/11/02
 * description    : 포트폴리오 알림 조회 DTO
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/11/02        piecejhm       최초 생성
 */


public class PortfolioNotiDTO {
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

        @SerializedName("notificationYn")
        @Expose
        private String notificationYn;

        public String getNotificationYn() {
            return notificationYn;
        }

        public void setNotificationYn(String notificationYn) {
            this.notificationYn = notificationYn;
        }

    }
}
