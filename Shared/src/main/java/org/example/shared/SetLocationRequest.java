package org.example.shared;

import org.example.shared.enums.RequestType;

public class SetLocationRequest extends Request{
    private final double latitude;
    private final double longitude;

    public SetLocationRequest(double latitude, double longitude) {
        super(RequestType.SET_LOCATION);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
