package com.cgi.irman.testifier;

import javax.persistence.Column;
import java.math.BigInteger;
import java.util.Date;

public class TradeModel {

    @javax.persistence.Id
    @Column(name = "id")
    private BigInteger Id;

    @Column(name = "trade_id")
    private String tradeId;

    @Column(name = "trade_version")
    private Long tradeVersion;

    @Column(name = "country_party_id")
    private String countryPartyId;

    @Column(name = "book_id")
    private String bookId;

    @Column(name = "maturity_date")
    private Date maturityDate;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "expired")
    private Boolean expired;

    public TradeModel(String tradeId, Long tradeVersion, String countryPartyId
            , String bookId, Date maturityDate, Date createdDate, Boolean expired) {
        this.tradeId = tradeId;
        this.tradeVersion = tradeVersion;
        this.countryPartyId = countryPartyId;
        this.bookId = bookId;
        this.maturityDate = maturityDate;
        this.createdDate = createdDate;
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

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public BigInteger getId() {
        return Id;
    }

    public void setId(BigInteger id) {
        Id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TradeModel) {
            TradeModel td = (TradeModel) obj;
            if (td.Id != null && td.Id.equals(this.Id))
                return true;
            else {
                if (td.getTradeId() != null && td.getTradeId().equals(this.getTradeId())
                        && td.getTradeVersion() != null && td.getTradeVersion().equals(this.getTradeVersion()))
                    return true;
            }
        }
        return false;
    }
}
