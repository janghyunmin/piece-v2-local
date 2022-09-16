package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : CouponDTO
 * author         : piecejhm
 * date           : 2022/09/13
 * description    : 쿠폰 Response DTO
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/13        piecejhm       최초 생성
 */


public class CouponDTO {
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

        @SerializedName("couponId")
        @Expose
        private String couponId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private Object description;
        @SerializedName("couponType")
        @Expose
        private String couponType;
        @SerializedName("couponTypeName")
        @Expose
        private Object couponTypeName;
        @SerializedName("issueAt")
        @Expose
        private String issueAt;
        @SerializedName("useBeginAt")
        @Expose
        private String useBeginAt;
        @SerializedName("useEndAt")
        @Expose
        private String useEndAt;
        @SerializedName("imagePath")
        @Expose
        private Object imagePath;

        public String getCouponId() {
            return couponId;
        }

        public void setCouponId(String couponId) {
            this.couponId = couponId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public String getCouponType() {
            return couponType;
        }

        public void setCouponType(String couponType) {
            this.couponType = couponType;
        }

        public Object getCouponTypeName() {
            return couponTypeName;
        }

        public void setCouponTypeName(Object couponTypeName) {
            this.couponTypeName = couponTypeName;
        }

        public String getIssueAt() {
            return issueAt;
        }

        public void setIssueAt(String issueAt) {
            this.issueAt = issueAt;
        }

        public String getUseBeginAt() {
            return useBeginAt;
        }

        public void setUseBeginAt(String useBeginAt) {
            this.useBeginAt = useBeginAt;
        }

        public String getUseEndAt() {
            return useEndAt;
        }

        public void setUseEndAt(String useEndAt) {
            this.useEndAt = useEndAt;
        }

        public Object getImagePath() {
            return imagePath;
        }

        public void setImagePath(Object imagePath) {
            this.imagePath = imagePath;
        }

    }
}
