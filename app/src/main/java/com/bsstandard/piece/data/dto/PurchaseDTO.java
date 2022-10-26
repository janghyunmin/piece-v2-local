package com.bsstandard.piece.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.dto
 * fileName       : PurchaseDTO
 * author         : piecejhm
 * date           : 2022/10/07
 * description    : 회원 구매 목록 조회 요청시 필요 DTO
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/07        piecejhm       최초 생성
 */


public class PurchaseDTO {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("purchaseId")
        @Expose
        private String purchaseId;
        @SerializedName("memberId")
        @Expose
        private String memberId;
        @SerializedName("portfolioId")
        @Expose
        private String portfolioId;
        @SerializedName("purchaseState")
        @Expose
        private String purchaseState;
        @SerializedName("purchaseAt")
        @Expose
        private String purchaseAt;
        @SerializedName("purchasePieceVolume")
        @Expose
        private Integer purchasePieceVolume;
        @SerializedName("purchasePieceAmount")
        @Expose
        private Integer purchasePieceAmount;
        @SerializedName("purchaseTotalAmount")
        @Expose
        private Integer purchaseTotalAmount;
        @SerializedName("isCoupon")
        @Expose
        private String isCoupon;

        @SerializedName("isConfirm")
        @Expose
        private String isConfirm;

        @SerializedName("portfolio")
        @Expose
        private Portfolio portfolio;
        @SerializedName("document")
        @Expose
        private Object document;

        public String getPurchaseId() {
            return purchaseId;
        }

        public void setPurchaseId(String purchaseId) {
            this.purchaseId = purchaseId;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getPortfolioId() {
            return portfolioId;
        }

        public void setPortfolioId(String portfolioId) {
            this.portfolioId = portfolioId;
        }

        public String getPurchaseState() {
            return purchaseState;
        }

        public void setPurchaseState(String purchaseState) {
            this.purchaseState = purchaseState;
        }

        public String getPurchaseAt() {
            return purchaseAt;
        }

        public void setPurchaseAt(String purchaseAt) {
            this.purchaseAt = purchaseAt;
        }

        public Integer getPurchasePieceVolume() {
            return purchasePieceVolume;
        }

        public void setPurchasePieceVolume(Integer purchasePieceVolume) {
            this.purchasePieceVolume = purchasePieceVolume;
        }

        public Integer getPurchasePieceAmount() {
            return purchasePieceAmount;
        }

        public void setPurchasePieceAmount(Integer purchasePieceAmount) {
            this.purchasePieceAmount = purchasePieceAmount;
        }

        public Integer getPurchaseTotalAmount() {
            return purchaseTotalAmount;
        }

        public void setPurchaseTotalAmount(Integer purchaseTotalAmount) {
            this.purchaseTotalAmount = purchaseTotalAmount;
        }

        public String getIsCoupon() {
            return isCoupon;
        }

        public void setIsCoupon(String isCoupon) {
            this.isCoupon = isCoupon;
        }

        public String getIsConfirm() {
            return isConfirm;
        }

        public void setIsConfirm(String isConfirm) {
            this.isConfirm = isConfirm;
        }

        public Portfolio getPortfolio() {
            return portfolio;
        }

        public void setPortfolio(Portfolio portfolio) {
            this.portfolio = portfolio;
        }

        public Object getDocument() {
            return document;
        }

        public void setDocument(Object document) {
            this.document = document;
        }


        public class Portfolio {

            @SerializedName("portfolioId")
            @Expose
            private String portfolioId;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("representThumbnailImagePath")
            @Expose
            private String representThumbnailImagePath;
            @SerializedName("representImagePath")
            @Expose
            private String representImagePath;
            @SerializedName("recruitmentAmount")
            @Expose
            private String recruitmentAmount;
            @SerializedName("recruitmentBeginDate")
            @Expose
            private String recruitmentBeginDate;
            @SerializedName("minPurchaseAmount")
            @Expose
            private String minPurchaseAmount;
            @SerializedName("maxPurchaseAmount")
            @Expose
            private String maxPurchaseAmount;
            @SerializedName("dividendsExpecatationDate")
            @Expose
            private String dividendsExpecatationDate;
            @SerializedName("products")
            @Expose
            private List<Product> products = null;

            public String getPortfolioId() {
                return portfolioId;
            }

            public void setPortfolioId(String portfolioId) {
                this.portfolioId = portfolioId;
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

            public String getRepresentImagePath() {
                return representImagePath;
            }

            public void setRepresentImagePath(String representImagePath) {
                this.representImagePath = representImagePath;
            }

            public String getRecruitmentAmount() {
                return recruitmentAmount;
            }

            public void setRecruitmentAmount(String recruitmentAmount) {
                this.recruitmentAmount = recruitmentAmount;
            }

            public String getRecruitmentBeginDate() {
                return recruitmentBeginDate;
            }

            public void setRecruitmentBeginDate(String recruitmentBeginDate) {
                this.recruitmentBeginDate = recruitmentBeginDate;
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

            public String getDividendsExpecatationDate() {
                return dividendsExpecatationDate;
            }

            public void setDividendsExpecatationDate(String dividendsExpecatationDate) {
                this.dividendsExpecatationDate = dividendsExpecatationDate;
            }

            public List<Product> getProducts() {
                return products;
            }

            public void setProducts(List<Product> products) {
                this.products = products;
            }

            public class Product {

                @SerializedName("portfolioId")
                @Expose
                private Object portfolioId;
                @SerializedName("productId")
                @Expose
                private String productId;
                @SerializedName("title")
                @Expose
                private String title;
                @SerializedName("productCondition")
                @Expose
                private String productCondition;
                @SerializedName("productPackageCondition")
                @Expose
                private String productPackageCondition;
                @SerializedName("representThumbnailImagePath")
                @Expose
                private String representThumbnailImagePath;
                @SerializedName("representImagePath")
                @Expose
                private String representImagePath;
                @SerializedName("productionYear")
                @Expose
                private String productionYear;
                @SerializedName("author")
                @Expose
                private String author;
                @SerializedName("productMaterial")
                @Expose
                private String productMaterial;
                @SerializedName("productDetailInfo")
                @Expose
                private String productDetailInfo;
                @SerializedName("documents")
                @Expose
                private List<Document> documents = null;

                public Object getPortfolioId() {
                    return portfolioId;
                }

                public void setPortfolioId(Object portfolioId) {
                    this.portfolioId = portfolioId;
                }

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

                public String getProductCondition() {
                    return productCondition;
                }

                public void setProductCondition(String productCondition) {
                    this.productCondition = productCondition;
                }

                public String getProductPackageCondition() {
                    return productPackageCondition;
                }

                public void setProductPackageCondition(String productPackageCondition) {
                    this.productPackageCondition = productPackageCondition;
                }

                public String getRepresentThumbnailImagePath() {
                    return representThumbnailImagePath;
                }

                public void setRepresentThumbnailImagePath(String representThumbnailImagePath) {
                    this.representThumbnailImagePath = representThumbnailImagePath;
                }

                public String getRepresentImagePath() {
                    return representImagePath;
                }

                public void setRepresentImagePath(String representImagePath) {
                    this.representImagePath = representImagePath;
                }

                public String getProductionYear() {
                    return productionYear;
                }

                public void setProductionYear(String productionYear) {
                    this.productionYear = productionYear;
                }

                public String getAuthor() {
                    return author;
                }

                public void setAuthor(String author) {
                    this.author = author;
                }

                public String getProductMaterial() {
                    return productMaterial;
                }

                public void setProductMaterial(String productMaterial) {
                    this.productMaterial = productMaterial;
                }

                public String getProductDetailInfo() {
                    return productDetailInfo;
                }

                public void setProductDetailInfo(String productDetailInfo) {
                    this.productDetailInfo = productDetailInfo;
                }

                public List<Document> getDocuments() {
                    return documents;
                }

                public void setDocuments(List<Document> documents) {
                    this.documents = documents;
                }

                public class Document {

                    @SerializedName("productId")
                    @Expose
                    private String productId;
                    @SerializedName("documentId")
                    @Expose
                    private String documentId;
                    @SerializedName("documentName")
                    @Expose
                    private String documentName;
                    @SerializedName("documentImagePath")
                    @Expose
                    private String documentImagePath;

                    public String getProductId() {
                        return productId;
                    }

                    public void setProductId(String productId) {
                        this.productId = productId;
                    }

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

                    public String getDocumentImagePath() {
                        return documentImagePath;
                    }

                    public void setDocumentImagePath(String documentImagePath) {
                        this.documentImagePath = documentImagePath;
                    }

                }
            }
        }
    }


}
