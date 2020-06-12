package com.cgi.irman.testifier;


public class Trade {

    private String tradeId;
    private Long tradeVersion;
    private String countryPartyId;
    private String bookId;
    private String maturityDate;

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
}
