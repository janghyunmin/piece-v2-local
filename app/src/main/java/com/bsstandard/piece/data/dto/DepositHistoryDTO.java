package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : DepositHistory
 * author         : piecejhm
 * date           : 2022/09/28
 * description    : 회원 거래 내역 조회 요청시 DTO
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/28        piecejhm       최초 생성
 */

public class DepositHistoryDTO {
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

        @SerializedName("result")
        @Expose
        private List<Result> result = null;
        @SerializedName("totalCount")
        @Expose
        private Integer totalCount;
        @SerializedName("page")
        @Expose
        private Integer page;
        @SerializedName("length")
        @Expose
        private Integer length;

        public List<Result> getResult() {
            return result;
        }

        public void setResult(List<Result> result) {
            this.result = result;
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

        public class Result {

            @SerializedName("memberId")
            @Expose
            private String memberId;
            @SerializedName("seq")
            @Expose
            private Integer seq;
            @SerializedName("changeAmount")
            @Expose
            private Integer changeAmount;
            @SerializedName("remainAmount")
            @Expose
            private Integer remainAmount;
            @SerializedName("changeReason")
            @Expose
            private String changeReason;
            @SerializedName("changeReasonName")
            @Expose
            private String changeReasonName;
            @SerializedName("changeReasonDetail")
            @Expose
            private Object changeReasonDetail;
            @SerializedName("createdAt")
            @Expose
            private String createdAt;

            public String getMemberId() {
                return memberId;
            }

            public void setMemberId(String memberId) {
                this.memberId = memberId;
            }

            public Integer getSeq() {
                return seq;
            }

            public void setSeq(Integer seq) {
                this.seq = seq;
            }

            public Integer getChangeAmount() {
                return changeAmount;
            }

            public void setChangeAmount(Integer changeAmount) {
                this.changeAmount = changeAmount;
            }

            public Integer getRemainAmount() {
                return remainAmount;
            }

            public void setRemainAmount(Integer remainAmount) {
                this.remainAmount = remainAmount;
            }

            public String getChangeReason() {
                return changeReason;
            }

            public void setChangeReason(String changeReason) {
                this.changeReason = changeReason;
            }

            public String getChangeReasonName() {
                return changeReasonName;
            }

            public void setChangeReasonName(String changeReasonName) {
                this.changeReasonName = changeReasonName;
            }

            public Object getChangeReasonDetail() {
                return changeReasonDetail;
            }

            public void setChangeReasonDetail(Object changeReasonDetail) {
                this.changeReasonDetail = changeReasonDetail;
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
