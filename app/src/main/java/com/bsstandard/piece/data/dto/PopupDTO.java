package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : PopupDTO
 * author         : piecejhm
 * date           : 2022/10/29
 * description    : 팝업 조회 요청 DTO
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/29        piecejhm       최초 생성
 */


public class PopupDTO {

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

        @SerializedName("popupId")
        @Expose
        private String popupId;
        @SerializedName("popupTitle")
        @Expose
        private String popupTitle;
        @SerializedName("popupType")
        @Expose
        private String popupType;
        @SerializedName("popupTypeName")
        @Expose
        private String popupTypeName;
        @SerializedName("popupImagePath")
        @Expose
        private String popupImagePath;
        @SerializedName("popupLinkType")
        @Expose
        private String popupLinkType;
        @SerializedName("popupLinkTypeName")
        @Expose
        private Object popupLinkTypeName;
        @SerializedName("popupLinkUrl")
        @Expose
        private String popupLinkUrl;

        public String getPopupId() {
            return popupId;
        }

        public void setPopupId(String popupId) {
            this.popupId = popupId;
        }

        public String getPopupTitle() {
            return popupTitle;
        }

        public void setPopupTitle(String popupTitle) {
            this.popupTitle = popupTitle;
        }

        public String getPopupType() {
            return popupType;
        }

        public void setPopupType(String popupType) {
            this.popupType = popupType;
        }

        public String getPopupTypeName() {
            return popupTypeName;
        }

        public void setPopupTypeName(String popupTypeName) {
            this.popupTypeName = popupTypeName;
        }

        public String getPopupImagePath() {
            return popupImagePath;
        }

        public void setPopupImagePath(String popupImagePath) {
            this.popupImagePath = popupImagePath;
        }

        public String getPopupLinkType() {
            return popupLinkType;
        }

        public void setPopupLinkType(String popupLinkType) {
            this.popupLinkType = popupLinkType;
        }

        public Object getPopupLinkTypeName() {
            return popupLinkTypeName;
        }

        public void setPopupLinkTypeName(Object popupLinkTypeName) {
            this.popupLinkTypeName = popupLinkTypeName;
        }

        public String getPopupLinkUrl() {
            return popupLinkUrl;
        }

        public void setPopupLinkUrl(String popupLinkUrl) {
            this.popupLinkUrl = popupLinkUrl;
        }

    }

}
