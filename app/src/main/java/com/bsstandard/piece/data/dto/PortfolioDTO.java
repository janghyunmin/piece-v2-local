package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.dto.portfolio
 * fileName       : PortfolioDTo
 * author         : piecejhm
 * date           : 2022/07/14
 * description    : PortfolioDTO Class
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/14        piecejhm       최초 생성
 */
public class PortfolioDTO {
    @SerializedName("status")
    @Expose
    private String status;
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

        @SerializedName("portfolios")
        @Expose
        private List<Portfolio> portfolios = null;
        @SerializedName("totalCount")
        @Expose
        private Integer totalCount;
        @SerializedName("page")
        @Expose
        private Integer page;
        @SerializedName("length")
        @Expose
        private Integer length;

        public List<Portfolio> getPortfolios() {
            return portfolios;
        }

        public void setPortfolios(List<Portfolio> portfolios) {
            this.portfolios = portfolios;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public class Portfolio {

            @SerializedName("portfolioId")
            @Expose
            private String portfolioId;
            @SerializedName("representThumbnailImagePath")
            @Expose
            private String representThumbnailImagePath;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("recruitmentState")
            @Expose
            private String recruitmentState;
            @SerializedName("recruitmentAmount")
            @Expose
            private String recruitmentAmount;
            @SerializedName("expectationProfitRate")
            @Expose
            private String expectationProfitRate;
            @SerializedName("totalPieceVolume")
            @Expose
            private String totalPieceVolume;
            @SerializedName("minPurchaseAmount")
            @Expose
            private String minPurchaseAmount;
            @SerializedName("maxPurchaseAmount")
            @Expose
            private String maxPurchaseAmount;
            @SerializedName("magazineId")
            @Expose
            private Object magazineId;
            @SerializedName("recruitmentBeginDate")
            @Expose
            private String recruitmentBeginDate;
            @SerializedName("recruitmentEndDate")
            @Expose
            private String recruitmentEndDate;
            @SerializedName("purchaseEndDate")
            @Expose
            private Object purchaseEndDate;
            @SerializedName("dividendsExpecatationDate")
            @Expose
            private String dividendsExpecatationDate;
            @SerializedName("purchaseFeeMethod")
            @Expose
            private Object purchaseFeeMethod;
            @SerializedName("purchaseFee")
            @Expose
            private Object purchaseFee;
            @SerializedName("soldoutAt")
            @Expose
            private Object soldoutAt;
            @SerializedName("createdAt")
            @Expose
            private String createdAt;
            @SerializedName("generalGrade")
            @Expose
            private String generalGrade;
            @SerializedName("stabilityPoint")
            @Expose
            private String stabilityPoint;
            @SerializedName("cashabilityPoint")
            @Expose
            private String cashabilityPoint;
            @SerializedName("profitabilityPoint")
            @Expose
            private String profitabilityPoint;
            @SerializedName("shareUrl")
            @Expose
            private String shareUrl;
            @SerializedName("remainingPieceVolume")
            @Expose
            private String remainingPieceVolume;
            @SerializedName("purchaseGuides")
            @Expose
            private List<PurchaseGuide> purchaseGuides = null;
            @SerializedName("products")
            @Expose
            private List<Product> products = null;

            public String getPortfolioId() {
                return portfolioId;
            }

            public void setPortfolioId(String portfolioId) {
                this.portfolioId = portfolioId;
            }

            public String getRepresentThumbnailImagePath() {
                return representThumbnailImagePath;
            }

            public void setRepresentThumbnailImagePath(String representThumbnailImagePath) {
                this.representThumbnailImagePath = representThumbnailImagePath;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getRecruitmentState() {
                return recruitmentState;
            }

            public void setRecruitmentState(String recruitmentState) {
                this.recruitmentState = recruitmentState;
            }

            public String getRecruitmentAmount() {
                return recruitmentAmount;
            }

            public void setRecruitmentAmount(String recruitmentAmount) {
                this.recruitmentAmount = recruitmentAmount;
            }

            public String getExpectationProfitRate() {
                return expectationProfitRate;
            }

            public void setExpectationProfitRate(String expectationProfitRate) {
                this.expectationProfitRate = expectationProfitRate;
            }

            public String getTotalPieceVolume() {
                return totalPieceVolume;
            }

            public void setTotalPieceVolume(String totalPieceVolume) {
                this.totalPieceVolume = totalPieceVolume;
            }

            public String getMinPurchaseAmount() {
                return minPurchaseAmount;
            }

            public void setMinPurchaseAmount(String minPurchaseAmount) {
                this.minPurchaseAmount = minPurchaseAmount;
            }

            public String getMaxPurchaseAmount() {
                return maxPurchaseAmount;
            }

            public void setMaxPurchaseAmount(String maxPurchaseAmount) {
                this.maxPurchaseAmount = maxPurchaseAmount;
            }

            public Object getMagazineId() {
                return magazineId;
            }

            public void setMagazineId(Object magazineId) {
                this.magazineId = magazineId;
            }

            public String getRecruitmentBeginDate() {
                return recruitmentBeginDate;
            }

            public void setRecruitmentBeginDate(String recruitmentBeginDate) {
                this.recruitmentBeginDate = recruitmentBeginDate;
            }

            public String getRecruitmentEndDate() {
                return recruitmentEndDate;
            }

            public void setRecruitmentEndDate(String recruitmentEndDate) {
                this.recruitmentEndDate = recruitmentEndDate;
            }

            public Object getPurchaseEndDate() {
                return purchaseEndDate;
            }

            public void setPurchaseEndDate(Object purchaseEndDate) {
                this.purchaseEndDate = purchaseEndDate;
            }

            public String getDividendsExpecatationDate() {
                return dividendsExpecatationDate;
            }

            public void setDividendsExpecatationDate(String dividendsExpecatationDate) {
                this.dividendsExpecatationDate = dividendsExpecatationDate;
            }

            public Object getPurchaseFeeMethod() {
                return purchaseFeeMethod;
            }

            public void setPurchaseFeeMethod(Object purchaseFeeMethod) {
                this.purchaseFeeMethod = purchaseFeeMethod;
            }

            public Object getPurchaseFee() {
                return purchaseFee;
            }

            public void setPurchaseFee(Object purchaseFee) {
                this.purchaseFee = purchaseFee;
            }

            public Object getSoldoutAt() {
                return soldoutAt;
            }

            public void setSoldoutAt(Object soldoutAt) {
                this.soldoutAt = soldoutAt;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getGeneralGrade() {
                return generalGrade;
            }

            public void setGeneralGrade(String generalGrade) {
                this.generalGrade = generalGrade;
            }

            public String getStabilityPoint() {
                return stabilityPoint;
            }

            public void setStabilityPoint(String stabilityPoint) {
                this.stabilityPoint = stabilityPoint;
            }

            public String getCashabilityPoint() {
                return cashabilityPoint;
            }

            public void setCashabilityPoint(String cashabilityPoint) {
                this.cashabilityPoint = cashabilityPoint;
            }

            public String getProfitabilityPoint() {
                return profitabilityPoint;
            }

            public void setProfitabilityPoint(String profitabilityPoint) {
                this.profitabilityPoint = profitabilityPoint;
            }

            public String getShareUrl() {
                return shareUrl;
            }

            public void setShareUrl(String shareUrl) {
                this.shareUrl = shareUrl;
            }

            public String getRemainingPieceVolume() {
                return remainingPieceVolume;
            }

            public void setRemainingPieceVolume(String remainingPieceVolume) {
                this.remainingPieceVolume = remainingPieceVolume;
            }

            public List<PurchaseGuide> getPurchaseGuides() {
                return purchaseGuides;
            }

            public void setPurchaseGuides(List<PurchaseGuide> purchaseGuides) {
                this.purchaseGuides = purchaseGuides;
            }

            public List<Product> getProducts() {
                return products;
            }

            public void setProducts(List<Product> products) {
                this.products = products;
            }

            public class Product {

                @SerializedName("productId")
                @Expose
                private String productId;
                @SerializedName("title")
                @Expose
                private String title;
                @SerializedName("representThumbnailImagePath")
                @Expose
                private String representThumbnailImagePath;
                @SerializedName("productionYear")
                @Expose
                private String productionYear;
                @SerializedName("productMaterial")
                @Expose
                private String productMaterial;
                @SerializedName("productSize")
                @Expose
                private String productSize;
                @SerializedName("productDetailInfo")
                @Expose
                private Object productDetailInfo;
                @SerializedName("author")
                @Expose
                private String author;
                @SerializedName("productDocuments")
                @Expose
                private List<ProductDocument> productDocuments = null;

                public String getProductId() {
                    return productId;
                }

                public void setProductId(String productId) {
                    this.productId = productId;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getRepresentThumbnailImagePath() {
                    return representThumbnailImagePath;
                }

                public void setRepresentThumbnailImagePath(String representThumbnailImagePath) {
                    this.representThumbnailImagePath = representThumbnailImagePath;
                }

                public String getProductionYear() {
                    return productionYear;
                }

                public void setProductionYear(String productionYear) {
                    this.productionYear = productionYear;
                }

                public String getProductMaterial() {
                    return productMaterial;
                }

                public void setProductMaterial(String productMaterial) {
                    this.productMaterial = productMaterial;
                }

                public String getProductSize() {
                    return productSize;
                }

                public void setProductSize(String productSize) {
                    this.productSize = productSize;
                }

                public Object getProductDetailInfo() {
                    return productDetailInfo;
                }

                public void setProductDetailInfo(Object productDetailInfo) {
                    this.productDetailInfo = productDetailInfo;
                }

                public String getAuthor() {
                    return author;
                }

                public void setAuthor(String author) {
                    this.author = author;
                }

                public List<ProductDocument> getProductDocuments() {
                    return productDocuments;
                }

                public void setProductDocuments(List<ProductDocument> productDocuments) {
                    this.productDocuments = productDocuments;
                }

                public class ProductDocument {

                    @SerializedName("documentId")
                    @Expose
                    private String documentId;
                    @SerializedName("documentName")
                    @Expose
                    private String documentName;
                    @SerializedName("documentIconPath")
                    @Expose
                    private String documentIconPath;
                    @SerializedName("documentImagePath")
                    @Expose
                    private String documentImagePath;

                    public String getDocumentId() {
                        return documentId;
                    }

                    public void setDocumentId(String documentId) {
                        this.documentId = documentId;
                    }

                    public String getDocumentName() {
                        return documentName;
                    }

                    public void setDocumentName(String documentName) {
                        this.documentName = documentName;
                    }

                    public String getDocumentIconPath() {
                        return documentIconPath;
                    }

                    public void setDocumentIconPath(String documentIconPath) {
                        this.documentIconPath = documentIconPath;
                    }

                    public String getDocumentImagePath() {
                        return documentImagePath;
                    }

                    public void setDocumentImagePath(String documentImagePath) {
                        this.documentImagePath = documentImagePath;
                    }

                }

            }

            public class PurchaseGuide {

                @SerializedName("guideId")
                @Expose
                private String guideId;
                @SerializedName("guideName")
                @Expose
                private String guideName;
                @SerializedName("description")
                @Expose
                private Object description;
                @SerializedName("guideIconPath")
                @Expose
                private String guideIconPath;

                public String getGuideId() {
                    return guideId;
                }

                public void setGuideId(String guideId) {
                    this.guideId = guideId;
                }

                public String getGuideName() {
                    return guideName;
                }

                public void setGuideName(String guideName) {
                    this.guideName = guideName;
                }

                public Object getDescription() {
                    return description;
                }

                public void setDescription(Object description) {
                    this.description = description;
                }

                public String getGuideIconPath() {
                    return guideIconPath;
                }

                public void setGuideIconPath(String guideIconPath) {
                    this.guideIconPath = guideIconPath;
                }

            }


        }


    }

}
