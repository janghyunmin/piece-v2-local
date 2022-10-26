package com.bsstandard.piece.data.datamodel.dmodel.products;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.products
 * fileName       : ProductsList
 * author         : piecejhm
 * date           : 2022/10/14
 * description    : Deposit purchase products - title List
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/14        piecejhm       최초 생성
 */


public class ProductsList implements Parcelable {
    private String title;

    public ProductsList(String title) {
        this.title = title;
    }

    public ProductsList(Parcel src) {
        title = src.readString();
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public ProductsList createFromParcel(Parcel source) {
            return new ProductsList(source);
        }

        @Override
        public ProductsList[] newArray(int size) {
            return new ProductsList[size];
        }
    };

    @Override
    public int describeContents() { return  0;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
