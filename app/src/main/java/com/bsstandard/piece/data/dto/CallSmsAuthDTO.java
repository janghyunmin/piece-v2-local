package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : CallSmsAuthDTO
 * author         : piecejhm
 * date           : 2022/06/22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/22        piecejhm       최초 생성
 */
public class CallSmsAuthDTO {

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

        @SerializedName("txSeqNo")
        @Expose
        private String txSeqNo;
        @SerializedName("rsltCd")
        @Expose
        private String rsltCd;
        @SerializedName("cpCd")
        @Expose
        private String cpCd;
        @SerializedName("mdlTkn")
        @Expose
        private String mdlTkn;
        @SerializedName("rsltMsg")
        @Expose
        private String rsltMsg;
        @SerializedName("telComCd")
        @Expose
        private String telComCd;
        @SerializedName("resendCnt")
        @Expose
        private String resendCnt;

        public String getTxSeqNo() {
            return txSeqNo;
        }

        public void setTxSeqNo(String txSeqNo) {
            this.txSeqNo = txSeqNo;
        }

        public String getRsltCd() {
            return rsltCd;
        }

        public void setRsltCd(String rsltCd) {
            this.rsltCd = rsltCd;
        }

        public String getCpCd() {
            return cpCd;
        }

        public void setCpCd(String cpCd) {
            this.cpCd = cpCd;
        }

        public String getMdlTkn() {
            return mdlTkn;
        }

        public void setMdlTkn(String mdlTkn) {
            this.mdlTkn = mdlTkn;
        }

        public String getRsltMsg() {
            return rsltMsg;
        }

        public void setRsltMsg(String rsltMsg) {
            this.rsltMsg = rsltMsg;
        }

        public String getTelComCd() {
            return telComCd;
        }

        public void setTelComCd(String telComCd) {
            this.telComCd = telComCd;
        }

        public String getResendCnt() {
            return resendCnt;
        }

        public void setResendCnt(String resendCnt) {
            this.resendCnt = resendCnt;
        }

    }
}
