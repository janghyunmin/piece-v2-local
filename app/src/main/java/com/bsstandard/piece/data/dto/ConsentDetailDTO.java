package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * packageName    : com.bsstandard.piece.data.dao.remote
 * fileName       : ConsentDetailDAO
 * author         : piecejhm
 * date           : 2022/06/21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/21        piecejhm       최초 생성
 */
public class ConsentDetailDTO {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
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

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
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

        @SerializedName("consentCode")
        @Expose
        private String consentCode;
        @SerializedName("consentGroup")
        @Expose
        private String consentGroup;
        @SerializedName("consentTitle")
        @Expose
        private String consentTitle;
        @SerializedName("consentContents")
        @Expose
        private String consentContents;
        @SerializedName("isMandatory")
        @Expose
        private String isMandatory;
        @SerializedName("displayOrder")
        @Expose
        private Integer displayOrder;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        public String getConsentCode() {
            return consentCode;
        }

        public void setConsentCode(String consentCode) {
            this.consentCode = consentCode;
        }

        public String getConsentGroup() {
            return consentGroup;
        }

        public void setConsentGroup(String consentGroup) {
            this.consentGroup = consentGroup;
        }

        public String getConsentTitle() {
            return consentTitle;
        }

        public void setConsentTitle(String consentTitle) {
            this.consentTitle = consentTitle;
        }

        public String getConsentContents() {
            return consentContents;
        }

        public void setConsentContents(String consentContents) {
            this.consentContents = consentContents;
        }

        public String getIsMandatory() {
            return isMandatory;
        }

        public void setIsMandatory(String isMandatory) {
            this.isMandatory = isMandatory;
        }

        public Integer getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }

}
