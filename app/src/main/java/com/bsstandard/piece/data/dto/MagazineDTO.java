package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : MagazineDTO
 * author         : piecejhm
 * date           : 2022/08/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/25        piecejhm       최초 생성
 */
public class MagazineDTO {
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

        @SerializedName("magazines")
        @Expose
        private List<Magazine> magazines = null;
        @SerializedName("totalCount")
        @Expose
        private Integer totalCount;
        @SerializedName("page")
        @Expose
        private Integer page;
        @SerializedName("length")
        @Expose
        private Integer length;

        public List<Magazine> getMagazines() {
            return magazines;
        }

        public void setMagazines(List<Magazine> magazines) {
            this.magazines = magazines;
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

        public class Magazine {

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
            private Object representImagePath;
            @SerializedName("contents")
            @Expose
            private Object contents;
            @SerializedName("isDelete")
            @Expose
            private Object isDelete;
            @SerializedName("createdAt")
            @Expose
            private String createdAt;
            @SerializedName("isFavorite")
            @Expose
            private String isFavorite;

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

            public Object getRepresentImagePath() {
                return representImagePath;
            }

            public void setRepresentImagePath(Object representImagePath) {
                this.representImagePath = representImagePath;
            }

            public Object getContents() {
                return contents;
            }

            public void setContents(Object contents) {
                this.contents = contents;
            }

            public Object getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(Object isDelete) {
                this.isDelete = isDelete;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getIsFavorite() {
                return isFavorite;
            }

            public void setIsFavorite(String isFavorite) {
                this.isFavorite = isFavorite;
            }

        }
    }

}
