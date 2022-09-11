package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : BoardDTO
 * author         : piecejhm
 * date           : 2022/09/10
 * description    : 공지사항 DTO
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/10        piecejhm       최초 생성
 */

public class BoardDTO {
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

        @SerializedName("boards")
        @Expose
        private List<Board> boards = null;
        @SerializedName("totalCount")
        @Expose
        private Integer totalCount;
        @SerializedName("page")
        @Expose
        private Integer page;
        @SerializedName("length")
        @Expose
        private Integer length;

        public List<Board> getBoards() {
            return boards;
        }

        public void setBoards(List<Board> boards) {
            this.boards = boards;
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

        public class Board {

            @SerializedName("boardId")
            @Expose
            private String boardId;
            @SerializedName("boardType")
            @Expose
            private String boardType;
            @SerializedName("boardCategory")
            @Expose
            private String boardCategory;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("press")
            @Expose
            private Object press;
            @SerializedName("contents")
            @Expose
            private String contents;
            @SerializedName("boadrThumbnailPath")
            @Expose
            private Object boadrThumbnailPath;
            @SerializedName("createdAt")
            @Expose
            private String createdAt;

            public String getBoardId() {
                return boardId;
            }

            public void setBoardId(String boardId) {
                this.boardId = boardId;
            }

            public String getBoardType() {
                return boardType;
            }

            public void setBoardType(String boardType) {
                this.boardType = boardType;
            }

            public String getBoardCategory() {
                return boardCategory;
            }

            public void setBoardCategory(String boardCategory) {
                this.boardCategory = boardCategory;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Object getPress() {
                return press;
            }

            public void setPress(Object press) {
                this.press = press;
            }

            public String getContents() {
                return contents;
            }

            public void setContents(String contents) {
                this.contents = contents;
            }

            public Object getBoadrThumbnailPath() {
                return boadrThumbnailPath;
            }

            public void setBoadrThumbnailPath(Object boadrThumbnailPath) {
                this.boadrThumbnailPath = boadrThumbnailPath;
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
