package org.example.shared;

import org.example.shared.enums.RequestType;

import java.io.Serializable;

public abstract class Request implements Serializable {
    private final RequestType requestType;

    public Request(RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}
