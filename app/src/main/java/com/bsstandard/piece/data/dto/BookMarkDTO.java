package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : BookMarkDTO
 * author         : piecejhm
 * date           : 2022/08/30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/30        piecejhm       최초 생성
 */
public class BookMarkDTO {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("memberId")
        @Expose
        private String memberId;
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
        private Object author;
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

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

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

        public Object getAuthor() {
            return author;
        }

        public void setAuthor(Object author) {
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

    }
}
