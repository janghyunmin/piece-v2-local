package com.bsstandard.piece.data.datamodel.dmodel.consent;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.consent
 * fileName       : UpdataConsentList
 * author         : piecejhm
 * date           : 2022/09/06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/06        piecejhm       최초 생성
 */
public class UpdateConsentList implements Parcelable {
    private String memberId;
    private String consentCode;
    private String isAgreement;

    public UpdateConsentList(String memberId, String consentCode, String isAgreement) {
        this.memberId = memberId;
        this.consentCode = consentCode;
        this.isAgreement = isAgreement;
    }

    public UpdateConsentList(Parcel src) {
        memberId = src.readString();
        consentCode = src.readString();
        isAgreement = src.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public UpdateConsentList createFromParcel(Parcel source) {
            return new UpdateConsentList(source);
        }

        @Override
        public UpdateConsentList[] newArray(int size) {
            return new UpdateConsentList[size];
        }
    };

    @Override
    public int describeContents() { return  0;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(memberId);
        dest.writeString(consentCode);
        dest.writeString(isAgreement);
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getConsentCode() {
        return consentCode;
    }

    public void setConsentCode(String consentCode) {
        this.consentCode = consentCode;
    }

    public String getIsAgreement() {
        return isAgreement;
    }

    public void setIsAgreement(String isAgreement) {
        this.isAgreement = isAgreement;
    }
}
