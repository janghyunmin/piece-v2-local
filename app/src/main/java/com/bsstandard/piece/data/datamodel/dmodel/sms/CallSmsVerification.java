package com.bsstandard.piece.data.datamodel.dmodel.sms;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.sms
 * fileName       : CallSmsVerification
 * author         : piecejhm
 * date           : 2022/10/28
 * description    : 본인 인증 sms 검증 요청 Model
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/28        piecejhm       최초 생성
 */
public class CallSmsVerification {
    private String txSeqNo;
    private String telNo;
    private String otpNo;

    public CallSmsVerification(String txSeqNo, String telNo, String otpNo) {
        this.txSeqNo = txSeqNo;
        this.telNo = telNo;
        this.otpNo = otpNo;
    }

    public String getTxSeqNo() {
        return txSeqNo;
    }

    public void setTxSeqNo(String txSeqNo) {
        this.txSeqNo = txSeqNo;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getOtpNo() {
        return otpNo;
    }

    public void setOtpNo(String otpNo) {
        this.otpNo = otpNo;
    }
}
