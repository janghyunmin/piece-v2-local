package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : EventDTO
 * author         : piecejhm
 * date           : 2022/09/11
 * description    : 이벤트 목록 조회 DTO
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/11        piecejhm       최초 생성
 */
public class EventDTO {
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

        @SerializedName("events")
        @Expose
        private List<Event> events = null;
        @SerializedName("totalCount")
        @Expose
        private Integer totalCount;
        @SerializedName("page")
        @Expose
        private Integer page;
        @SerializedName("length")
        @Expose
        private Integer length;

        public List<Event> getEvents() {
            return events;
        }

        public void setEvents(List<Event> events) {
            this.events = events;
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

        public class Event {

            @SerializedName("eventId")
            @Expose
            private String eventId;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("contents")
            @Expose
            private String contents;
            @SerializedName("eventBeginDate")
            @Expose
            private String eventBeginDate;
            @SerializedName("eventEndDate")
            @Expose
            private String eventEndDate;
            @SerializedName("representThumbnailPath")
            @Expose
            private String representThumbnailPath;
            @SerializedName("createdAt")
            @Expose
            private String createdAt;
            @SerializedName("shareUrl")
            @Expose
            private String shareUrl;
            @SerializedName("eventButtons")
            @Expose
            private List<EventButton> eventButtons = null;

            public String getEventId() {
                return eventId;
            }

            public void setEventId(String eventId) {
                this.eventId = eventId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContents() {
                return contents;
            }

            public void setContents(String contents) {
                this.contents = contents;
            }

            public String getEventBeginDate() {
                return eventBeginDate;
            }

            public void setEventBeginDate(String eventBeginDate) {
                this.eventBeginDate = eventBeginDate;
            }

            public String getEventEndDate() {
                return eventEndDate;
            }

            public void setEventEndDate(String eventEndDate) {
                this.eventEndDate = eventEndDate;
            }

            public String getRepresentThumbnailPath() {
                return representThumbnailPath;
            }

            public void setRepresentThumbnailPath(String representThumbnailPath) {
                this.representThumbnailPath = representThumbnailPath;
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

            public List<EventButton> getEventButtons() {
                return eventButtons;
            }

            public void setEventButtons(List<EventButton> eventButtons) {
                this.eventButtons = eventButtons;
            }
            public class EventButton {

                @SerializedName("eventId")
                @Expose
                private String eventId;
                @SerializedName("seq")
                @Expose
                private Integer seq;
                @SerializedName("btnTitle")
                @Expose
                private String btnTitle;
                @SerializedName("btnType")
                @Expose
                private String btnType;
                @SerializedName("btnEndpoint")
                @Expose
                private String btnEndpoint;
                @SerializedName("btnEndpointAuth")
                @Expose
                private String btnEndpointAuth;
                @SerializedName("createdAt")
                @Expose
                private String createdAt;

                public String getEventId() {
                    return eventId;
                }

                public void setEventId(String eventId) {
                    this.eventId = eventId;
                }

                public Integer getSeq() {
                    return seq;
                }

                public void setSeq(Integer seq) {
                    this.seq = seq;
                }

                public String getBtnTitle() {
                    return btnTitle;
                }

                public void setBtnTitle(String btnTitle) {
                    this.btnTitle = btnTitle;
                }

                public String getBtnType() {
                    return btnType;
                }

                public void setBtnType(String btnType) {
                    this.btnType = btnType;
                }

                public String getBtnEndpoint() {
                    return btnEndpoint;
                }

                public void setBtnEndpoint(String btnEndpoint) {
                    this.btnEndpoint = btnEndpoint;
                }

                public String getBtnEndpointAuth() {
                    return btnEndpointAuth;
                }

                public void setBtnEndpointAuth(String btnEndpointAuth) {
                    this.btnEndpointAuth = btnEndpointAuth;
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



}
