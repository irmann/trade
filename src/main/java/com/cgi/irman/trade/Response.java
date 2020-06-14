package com.cgi.irman.trade;

import java.util.ArrayList;
import java.util.List;

public class Response {
    private int status;
    private String message;
    private int errorCode;
    private List<TradeModel> payload = new ArrayList<>();

    public Response(int status, String message, int errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }

    public Response(int status, String message, int errorCode, List<TradeModel> payload) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.payload = payload;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<TradeModel> getPayload() {
        return payload;
    }

    public void setPayload(List<TradeModel> payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return status == response.status ;
    }
}
