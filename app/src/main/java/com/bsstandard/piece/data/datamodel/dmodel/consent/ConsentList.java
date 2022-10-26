package com.bsstandard.piece.data.datamodel.dmodel.consent;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * packageName    : com.bsstandard.piece.data.dao.local
 * fileName       : ConsentList
 * author         : piecejhm
 * date           : 2022/06/17
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/17        piecejhm       최초 생성
 */
public class ConsentList implements Parcelable{
    private boolean isChk;
    private String consentCode;
    private String consentGroup;
    private String consentTitle;
    private String consentContents;
    private String isMandatory;
    private Integer displayOrder;
    private String createdAt;
    private String isAgreement;
    private int viewType;

    public ConsentList(boolean isChk ,String consentCode,String consentGroup,String consetTitle,String consetContents,String isMandatory,Integer displayOrder, String createdAt , String isAgreement , int viewType){
        this.isChk = isChk;
        this.consentCode = consentCode;
        this.consentGroup = consentGroup;
        this.consentTitle = consetTitle;
        this.consentContents = consetContents;
        this.isMandatory = isMandatory;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.isAgreement = isAgreement;
        this.viewType = viewType;
    }

    public ConsentList(Parcel src){
        consentCode = src.readString();
        consentGroup = src.readString();
        isAgreement = src.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public ConsentList createFromParcel(Parcel source) {
            return new ConsentList(source);
        }

        @Override
        public ConsentList[] newArray(int size) {
            return new ConsentList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest , int flags) {
        dest.writeString(consentCode);
        dest.writeString(consentGroup);
        dest.writeString(isAgreement);
    }


    public boolean isChk() {
        return isChk;
    }

    public void setChk(boolean chk) {
        isChk = chk;
    }

    public String getConsentCode() {
        return consentCode;
    }

    public void setConsentCode(String consentCode) {
        this.consentCode = consentCode;
    }

    public String getConsentGroup() {
        return consentGroup;
    }

    public void setConsentGroup(String consentGroup) {
        this.consentGroup = consentGroup;
    }

    public String getConsentTitle() {
        return consentTitle;
    }

    public void setConsentTitle(String consentTitle) {
        this.consentTitle = consentTitle;
    }

    public String getConsentContents() {
        return consentContents;
    }

    public void setConsentContents(String consentContents) {
        this.consentContents = consentContents;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
