package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : LocationDTO
 * author         : piecejhm
 * date           : 2022/09/04
 * description    : 주소검색시 필요 Dto
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/04        piecejhm       최초 생성
 */


public class LocationDTO {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("results")
        @Expose
        private Results results;

        public Results getResults() {
            return results;
        }

        public void setResults(Results results) {
            this.results = results;
        }

        public class Results {

            @SerializedName("common")
            @Expose
            private Common common;
            @SerializedName("juso")
            @Expose
            private List<Juso> juso = null;

            public Common getCommon() {
                return common;
            }

            public void setCommon(Common common) {
                this.common = common;
            }

            public List<Juso> getJuso() {
                return juso;
            }

            public void setJuso(List<Juso> juso) {
                this.juso = juso;
            }

            public class Common {

                @SerializedName("errorMessage")
                @Expose
                private String errorMessage;
                @SerializedName("countPerPage")
                @Expose
                private String countPerPage;
                @SerializedName("errorCode")
                @Expose
                private String errorCode;
                @SerializedName("totalCount")
                @Expose
                private String totalCount;
                @SerializedName("currentPage")
                @Expose
                private String currentPage;

                public String getErrorMessage() {
                    return errorMessage;
                }

                public void setErrorMessage(String errorMessage) {
                    this.errorMessage = errorMessage;
                }

                public String getCountPerPage() {
                    return countPerPage;
                }

                public void setCountPerPage(String countPerPage) {
                    this.countPerPage = countPerPage;
                }

                public String getErrorCode() {
                    return errorCode;
                }

                public void setErrorCode(String errorCode) {
                    this.errorCode = errorCode;
                }

                public String getTotalCount() {
                    return totalCount;
                }

                public void setTotalCount(String totalCount) {
                    this.totalCount = totalCount;
                }

                public String getCurrentPage() {
                    return currentPage;
                }

                public void setCurrentPage(String currentPage) {
                    this.currentPage = currentPage;
                }

            }

            public class Juso {

                @SerializedName("siNm")
                @Expose
                private String siNm;
                @SerializedName("lnbrMnnm")
                @Expose
                private String lnbrMnnm;
                @SerializedName("bdKdcd")
                @Expose
                private String bdKdcd;
                @SerializedName("jibunAddr")
                @Expose
                private String jibunAddr;
                @SerializedName("buldSlno")
                @Expose
                private String buldSlno;
                @SerializedName("bdMgtSn")
                @Expose
                private String bdMgtSn;
                @SerializedName("zipNo")
                @Expose
                private String zipNo;
                @SerializedName("admCd")
                @Expose
                private String admCd;
                @SerializedName("roadAddr")
                @Expose
                private String roadAddr;
                @SerializedName("liNm")
                @Expose
                private String liNm;
                @SerializedName("bdNm")
                @Expose
                private String bdNm;
                @SerializedName("mtYn")
                @Expose
                private String mtYn;
                @SerializedName("rnMgtSn")
                @Expose
                private String rnMgtSn;
                @SerializedName("roadAddrPart2")
                @Expose
                private String roadAddrPart2;
                @SerializedName("sggNm")
                @Expose
                private String sggNm;
                @SerializedName("buldMnnm")
                @Expose
                private String buldMnnm;
                @SerializedName("roadAddrPart1")
                @Expose
                private String roadAddrPart1;
                @SerializedName("emdNm")
                @Expose
                private String emdNm;
                @SerializedName("lnbrSlno")
                @Expose
                private String lnbrSlno;
                @SerializedName("engAddr")
                @Expose
                private String engAddr;
                @SerializedName("udrtYn")
                @Expose
                private String udrtYn;
                @SerializedName("rn")
                @Expose
                private String rn;
                @SerializedName("detBdNmList")
                @Expose
                private String detBdNmList;
                @SerializedName("emdNo")
                @Expose
                private String emdNo;

                public String getSiNm() {
                    return siNm;
                }

                public void setSiNm(String siNm) {
                    this.siNm = siNm;
                }

                public String getLnbrMnnm() {
                    return lnbrMnnm;
                }

                public void setLnbrMnnm(String lnbrMnnm) {
                    this.lnbrMnnm = lnbrMnnm;
                }

                public String getBdKdcd() {
                    return bdKdcd;
                }

                public void setBdKdcd(String bdKdcd) {
                    this.bdKdcd = bdKdcd;
                }

                public String getJibunAddr() {
                    return jibunAddr;
                }

                public void setJibunAddr(String jibunAddr) {
                    this.jibunAddr = jibunAddr;
                }

                public String getBuldSlno() {
                    return buldSlno;
                }

                public void setBuldSlno(String buldSlno) {
                    this.buldSlno = buldSlno;
                }

                public String getBdMgtSn() {
                    return bdMgtSn;
                }

                public void setBdMgtSn(String bdMgtSn) {
                    this.bdMgtSn = bdMgtSn;
                }

                public String getZipNo() {
                    return zipNo;
                }

                public void setZipNo(String zipNo) {
                    this.zipNo = zipNo;
                }

                public String getAdmCd() {
                    return admCd;
                }

                public void setAdmCd(String admCd) {
                    this.admCd = admCd;
                }

                public String getRoadAddr() {
                    return roadAddr;
                }

                public void setRoadAddr(String roadAddr) {
                    this.roadAddr = roadAddr;
                }

                public String getLiNm() {
                    return liNm;
                }

                public void setLiNm(String liNm) {
                    this.liNm = liNm;
                }

                public String getBdNm() {
                    return bdNm;
                }

                public void setBdNm(String bdNm) {
                    this.bdNm = bdNm;
                }

                public String getMtYn() {
                    return mtYn;
                }

                public void setMtYn(String mtYn) {
                    this.mtYn = mtYn;
                }

                public String getRnMgtSn() {
                    return rnMgtSn;
                }

                public void setRnMgtSn(String rnMgtSn) {
                    this.rnMgtSn = rnMgtSn;
                }

                public String getRoadAddrPart2() {
                    return roadAddrPart2;
                }

                public void setRoadAddrPart2(String roadAddrPart2) {
                    this.roadAddrPart2 = roadAddrPart2;
                }

                public String getSggNm() {
                    return sggNm;
                }

                public void setSggNm(String sggNm) {
                    this.sggNm = sggNm;
                }

                public String getBuldMnnm() {
                    return buldMnnm;
                }

                public void setBuldMnnm(String buldMnnm) {
                    this.buldMnnm = buldMnnm;
                }

                public String getRoadAddrPart1() {
                    return roadAddrPart1;
                }

                public void setRoadAddrPart1(String roadAddrPart1) {
                    this.roadAddrPart1 = roadAddrPart1;
                }

                public String getEmdNm() {
                    return emdNm;
                }

                public void setEmdNm(String emdNm) {
                    this.emdNm = emdNm;
                }

                public String getLnbrSlno() {
                    return lnbrSlno;
                }

                public void setLnbrSlno(String lnbrSlno) {
                    this.lnbrSlno = lnbrSlno;
                }

                public String getEngAddr() {
                    return engAddr;
                }

                public void setEngAddr(String engAddr) {
                    this.engAddr = engAddr;
                }

                public String getUdrtYn() {
                    return udrtYn;
                }

                public void setUdrtYn(String udrtYn) {
                    this.udrtYn = udrtYn;
                }

                public String getRn() {
                    return rn;
                }

                public void setRn(String rn) {
                    this.rn = rn;
                }

                public String getDetBdNmList() {
                    return detBdNmList;
                }

                public void setDetBdNmList(String detBdNmList) {
                    this.detBdNmList = detBdNmList;
                }

                public String getEmdNo() {
                    return emdNo;
                }

                public void setEmdNo(String emdNo) {
                    this.emdNo = emdNo;
                }

            }
        }
    }


}
