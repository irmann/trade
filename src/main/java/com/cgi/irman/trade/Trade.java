package com.cgi.irman.trade;


public class Trade {

    private String tradeId;
    private Long tradeVersion;
    private String countryPartyId;
    private String bookId;
    private String maturityDate;
    private String createdDate;
    private Boolean expired;

    public Trade() {}

    public Trade(String tradeId, Long tradeVersion, String countryPartyId, String bookId, String maturityDate, Boolean expired) {
        this.tradeId = tradeId;
        this.tradeVersion = tradeVersion;
        this.countryPartyId = countryPartyId;
        this.bookId = bookId;
        this.maturityDate = maturityDate;
        this.expired = expired;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public Long getTradeVersion() {
        return tradeVersion;
    }

    public void setTradeVersion(Long tradeVersion) {
        this.tradeVersion = tradeVersion;
    }

    public String getCountryPartyId() {
        return countryPartyId;
    }

    public void setCountryPartyId(String countryPartyId) {
        this.countryPartyId = countryPartyId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }
    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}
