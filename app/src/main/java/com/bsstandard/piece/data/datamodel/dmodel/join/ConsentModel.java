package com.bsstandard.piece.data.datamodel.dmodel.join;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.join
 * fileName       : Consent
 * author         : piecejhm
 * date           : 2022/06/30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/30        piecejhm       최초 생성
 */

public class ConsentModel implements Parcelable{
    private String consentCode;
    private String isAgreement;

    public ConsentModel(String consentCode, String isAgreement){
        this.consentCode = consentCode;
        this.isAgreement = isAgreement;
    }

    public ConsentModel(Parcel src){
        consentCode = src.readString();
        isAgreement = src.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public ConsentModel createFromParcel(Parcel source) {
            return new ConsentModel(source);
        }

        @Override
        public ConsentModel[] newArray(int size) {
            return new ConsentModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(consentCode);
        dest.writeString(isAgreement);
    }

    public String getConsentCode() {
        return consentCode;
    }

    public void setConsentCode(String consentCode) {
        this.consentCode = consentCode;
    }

    public ConsentModel withConsentCode(String consentCode) {
        this.consentCode = consentCode;
        return this;
    }

    public String getIsAgreement() {
        return isAgreement;
    }

    public void setIsAgreement(String isAgreement) {
        this.isAgreement = isAgreement;
    }

    public ConsentModel withIsAgreement(String isAgreement) {
        this.isAgreement = isAgreement;
        return this;
    }

}
