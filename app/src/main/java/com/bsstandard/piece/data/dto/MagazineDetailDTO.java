package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : MagazineDetailDTO
 * author         : piecejhm
 * date           : 2022/08/30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/30        piecejhm       최초 생성
 */
public class MagazineDetailDTO {
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
        @SerializedName("shareUrl")
        @Expose
        private String shareUrl;

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

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

    }
}
