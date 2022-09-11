package com.bsstandard.piece.data.datamodel.dmodel.bookmark;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel.bookmark
 * fileName       : BookMarkList
 * author         : piecejhm
 * date           : 2022/08/31
 * description    : 회원 북마크 조회시 필요한 리스트
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/31        piecejhm       최초 생성
 */


public class BookMarkList implements Parcelable{
    private String memberId;
    private String magazineId;
    private String magazineType;
    private String title;
    private String midTitle;
    private String smallTitle;
    private String author;
    private String representThumbnailPath;
    private String representImagePath;

    public BookMarkList(String memberId,String magazineId, String magazineType, String title,
                        String midTitle, String smallTitle, String author, String representThumbnailPath,
                        String representImagePath) {
        this.memberId = memberId;
        this.magazineId = magazineId;
        this.magazineType = magazineType;
        this.title = title;
        this.midTitle = midTitle;
        this.smallTitle = smallTitle;
        this.author = author;
        this.representThumbnailPath = representThumbnailPath;
        this.representImagePath = representImagePath;
    }

    public BookMarkList(Parcel src) {
        memberId = src.readString();
        magazineId = src.readString();
        magazineType = src.readString();
        title = src.readString();
        midTitle = src.readString();
        smallTitle = src.readString();
        author = src.readString();
        representThumbnailPath = src.readString();
        representImagePath = src.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public BookMarkList createFromParcel(Parcel source) {
            return new BookMarkList(source);
        }

        @Override
        public BookMarkList[] newArray(int size) {
            return new BookMarkList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest , int flags) {
        dest.writeString(memberId);
        dest.writeString(magazineId);
        dest.writeString(magazineType);
        dest.writeString(title);
        dest.writeString(midTitle);
        dest.writeString(smallTitle);
        dest.writeString(author);
        dest.writeString(representThumbnailPath);
        dest.writeString(representImagePath);
    }


    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMagazineId() {
        return magazineId;
    }

    public void setMagazineId(String magazineId) {
        this.magazineId = magazineId;
    }

    public String getMagazineType() {
        return magazineType;
    }

    public void setMagazineType(String magazineType) {
        this.magazineType = magazineType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMidTitle() {
        return midTitle;
    }

    public void setMidTitle(String midTitle) {
        this.midTitle = midTitle;
    }

    public String getSmallTitle() {
        return smallTitle;
    }

    public void setSmallTitle(String smallTitle) {
        this.smallTitle = smallTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRepresentThumbnailPath() {
        return representThumbnailPath;
    }

    public void setRepresentThumbnailPath(String representThumbnailPath) {
        this.representThumbnailPath = representThumbnailPath;
    }

    public String getRepresentImagePath() {
        return representImagePath;
    }

    public void setRepresentImagePath(String representImagePath) {
        this.representImagePath = representImagePath;
    }
}
