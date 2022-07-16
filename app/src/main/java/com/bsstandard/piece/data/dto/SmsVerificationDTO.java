package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : SmsVerificationDTO
 * author         : piecejhm
 * date           : 2022/06/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/24        piecejhm       최초 생성
 */
public class SmsVerificationDTO {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("rsltCd")
        @Expose
        private String rsltCd;
        @SerializedName("rsltMsg")
        @Expose
        private String rsltMsg;
        @SerializedName("cpCd")
        @Expose
        private String cpCd;
        @SerializedName("txSeqNo")
        @Expose
        private String txSeqNo;
        @SerializedName("di")
        @Expose
        private String di;
        @SerializedName("ci")
        @Expose
        private String ci;
        @SerializedName("ci2")
        @Expose
        private String ci2;
        @SerializedName("ciUpdate")
        @Expose
        private String ciUpdate;
        @SerializedName("rsltName")
        @Expose
        private String rsltName;
        @SerializedName("telNo")
        @Expose
        private String telNo;
        @SerializedName("telComCd")
        @Expose
        private String telComCd;
        @SerializedName("rsltBirthday")
        @Expose
        private String rsltBirthday;
        @SerializedName("rsltSexCd")
        @Expose
        private String rsltSexCd;
        @SerializedName("rsltNtvFrnrCd")
        @Expose
        private String rsltNtvFrnrCd;

        public String getRsltCd() {
            return rsltCd;
        }

        public void setRsltCd(String rsltCd) {
            this.rsltCd = rsltCd;
        }

        public String getRsltMsg() {
            return rsltMsg;
        }

        public void setRsltMsg(String rsltMsg) {
            this.rsltMsg = rsltMsg;
        }

        public String getCpCd() {
            return cpCd;
        }

        public void setCpCd(String cpCd) {
            this.cpCd = cpCd;
        }

        public String getTxSeqNo() {
            return txSeqNo;
        }

        public void setTxSeqNo(String txSeqNo) {
            this.txSeqNo = txSeqNo;
        }

        public String getDi() {
            return di;
        }

        public void setDi(String di) {
            this.di = di;
        }

        public String getCi() {
            return ci;
        }

        public void setCi(String ci) {
            this.ci = ci;
        }

        public String getCi2() {
            return ci2;
        }

        public void setCi2(String ci2) {
            this.ci2 = ci2;
        }

        public String getCiUpdate() {
            return ciUpdate;
        }

        public void setCiUpdate(String ciUpdate) {
            this.ciUpdate = ciUpdate;
        }

        public String getRsltName() {
            return rsltName;
        }

        public void setRsltName(String rsltName) {
            this.rsltName = rsltName;
        }

        public String getTelNo() {
            return telNo;
        }

        public void setTelNo(String telNo) {
            this.telNo = telNo;
        }

        public String getTelComCd() {
            return telComCd;
        }

        public void setTelComCd(String telComCd) {
            this.telComCd = telComCd;
        }

        public String getRsltBirthday() {
            return rsltBirthday;
        }

        public void setRsltBirthday(String rsltBirthday) {
            this.rsltBirthday = rsltBirthday;
        }

        public String getRsltSexCd() {
            return rsltSexCd;
        }

        public void setRsltSexCd(String rsltSexCd) {
            this.rsltSexCd = rsltSexCd;
        }

        public String getRsltNtvFrnrCd() {
            return rsltNtvFrnrCd;
        }

        public void setRsltNtvFrnrCd(String rsltNtvFrnrCd) {
            this.rsltNtvFrnrCd = rsltNtvFrnrCd;
        }
    }
}
