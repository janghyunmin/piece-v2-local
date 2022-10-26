package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : AlarmDTO
 * author         : piecejhm
 * date           : 2022/10/16
 * description    : 알림 DTO
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/16        piecejhm       최초 생성
 */

public class AlarmDTO {
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

        @SerializedName("alarms")
        @Expose
        private List<Alarm> alarms = null;
        @SerializedName("totalCount")
        @Expose
        private Integer totalCount;
        @SerializedName("page")
        @Expose
        private Integer page;
        @SerializedName("length")
        @Expose
        private Integer length;

        public List<Alarm> getAlarms() {
            return alarms;
        }

        public void setAlarms(List<Alarm> alarms) {
            this.alarms = alarms;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public class Alarm {

            @SerializedName("notificationId")
            @Expose
            private String notificationId;
            @SerializedName("memberId")
            @Expose
            private String memberId;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("message")
            @Expose
            private String message;
            @SerializedName("notificationType")
            @Expose
            private String notificationType;
            @SerializedName("notificationTypeName")
            @Expose
            private Object notificationTypeName;
            @SerializedName("referralTarget")
            @Expose
            private Object referralTarget;
            @SerializedName("isRead")
            @Expose
            private String isRead;
            @SerializedName("createdAt")
            @Expose
            private String createdAt;

            public String getNotificationId() {
                return notificationId;
            }

            public void setNotificationId(String notificationId) {
                this.notificationId = notificationId;
            }

            public String getMemberId() {
                return memberId;
            }

            public void setMemberId(String memberId) {
                this.memberId = memberId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getNotificationType() {
                return notificationType;
            }

            public void setNotificationType(String notificationType) {
                this.notificationType = notificationType;
            }

            public Object getNotificationTypeName() {
                return notificationTypeName;
            }

            public void setNotificationTypeName(Object notificationTypeName) {
                this.notificationTypeName = notificationTypeName;
            }

            public Object getReferralTarget() {
                return referralTarget;
            }

            public void setReferralTarget(Object referralTarget) {
                this.referralTarget = referralTarget;
            }

            public String getIsRead() {
                return isRead;
            }

            public void setIsRead(String isRead) {
                this.isRead = isRead;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

        }
    }


}
