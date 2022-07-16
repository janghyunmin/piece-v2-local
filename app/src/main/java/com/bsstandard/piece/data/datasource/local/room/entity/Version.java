package com.bsstandard.piece.data.datasource.local.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * packageName    : com.bsstandard.piece.data.datasource.local.room.entity
 * fileName       : Version
 * author         : piecejhm
 * date           : 2022/07/01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/01        piecejhm       최초 생성
 */
@Entity
public class Version {
    @PrimaryKey(autoGenerate = false)
    private int id;
    private String appVersion;

    public Version(String appVersion){
        this.appVersion = appVersion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
    @Override
    public String toString() {
        return "\n id=> " + this.id + " , appVersion=> " + this.appVersion;
    }
}
