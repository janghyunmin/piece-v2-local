package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : DepositDTO
 * author         : piecejhm
 * date           : 2022/09/20
 * description    : 회원 예치금 잔행 조회 요청시 DTO
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/20        piecejhm       최초 생성
 */

public class DepositDTO {
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

        @SerializedName("depositBalance")
        @Expose
        private Integer depositBalance;
        @SerializedName("memberId")
        @Expose
        private String memberId;

        public Integer getDepositBalance() {
            return depositBalance;
        }

        public void setDepositBalance(Integer depositBalance) {
            this.depositBalance = depositBalance;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

    }
}
