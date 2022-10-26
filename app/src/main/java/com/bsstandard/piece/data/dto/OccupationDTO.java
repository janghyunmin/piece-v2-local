package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : OccupationDTO
 * author         : piecejhm
 * date           : 2022/10/06
 * description    : 휴대폰 점유 인증 요청 DTO
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/06        piecejhm       최초 생성
 */


public class OccupationDTO {

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

        @SerializedName("trdNo")
        @Expose
        private String trdNo;
        @SerializedName("mchtTrdNo")
        @Expose
        private String mchtTrdNo;

        public String getTrdNo() {
            return trdNo;
        }

        public void setTrdNo(String trdNo) {
            this.trdNo = trdNo;
        }

        public String getMchtTrdNo() {
            return mchtTrdNo;
        }

        public void setMchtTrdNo(String mchtTrdNo) {
            this.mchtTrdNo = mchtTrdNo;
        }

    }

}
