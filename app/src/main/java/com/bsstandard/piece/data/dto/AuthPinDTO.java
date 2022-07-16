package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : AuthPinDTO
 * author         : piecejhm
 * date           : 2022/07/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/04        piecejhm       최초 생성
 */

public class AuthPinDTO {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("data")
    @Expose
    private Object data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AuthPinDTO withStatus(String status) {
        this.status = status;
        return this;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public AuthPinDTO withMessage(Object message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public AuthPinDTO withData(Object data) {
        this.data = data;
        return this;
    }

}