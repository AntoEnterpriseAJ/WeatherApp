package org.example.shared.enums;

public enum RequestType {
    GET_WEATHER(1),
    UPDATE_WEATHER(2),
    REGISTER(3),
    LOGIN(4),
    SET_LOCATION(5);

    private final int value;

    RequestType(int value) {
        this.value = value;
    }

    public static RequestType fromInt(int requestTypeCode) {
        for (RequestType requestType : RequestType.values()) {
            if (requestType.value == requestTypeCode) {
                return requestType;
            }
        }
        throw new IllegalArgumentException("Invalid RequestType code");
    }
}
