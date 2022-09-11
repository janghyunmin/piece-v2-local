package com.bsstandard.piece.data.datamodel.dmodel.address;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.address
 * fileName       : AddressList
 * author         : piecejhm
 * date           : 2022/09/04
 * description    : 주소검색시 사용되는 ArrayList
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/04        piecejhm       최초 생성
 */
public class AddressList implements Parcelable {
    private String jibunAddr;
    private String roadAddr;

    public AddressList(String jibunAddr, String roadAddr){
        this.jibunAddr = jibunAddr;
        this.roadAddr = roadAddr;
    }

    public AddressList(Parcel src) {
        jibunAddr = src.readString();
        roadAddr = src.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public AddressList createFromParcel(Parcel source) {
            return new AddressList(source);
        }

        @Override
        public AddressList[] newArray(int size) {
            return new AddressList[size];
        }
    };

    @Override
    public int describeContents() { return 0;}

    @Override
    public void writeToParcel(Parcel dest , int flags) {
        dest.writeString(jibunAddr);
        dest.writeString(roadAddr);
    }

    public String getJibunAddr() {
        return jibunAddr;
    }

    public void setJibunAddr(String jibunAddr) {
        this.jibunAddr = jibunAddr;
    }

    public String getRoadAddr() {
        return roadAddr;
    }

    public void setRoadAddr(String roadAddr) {
        this.roadAddr = roadAddr;
    }
}
