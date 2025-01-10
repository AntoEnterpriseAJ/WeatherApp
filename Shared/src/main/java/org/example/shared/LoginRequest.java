package org.example.shared;

import org.example.shared.enums.RequestType;

public class LoginRequest extends Request {
    private String username;
    private String password;

    public LoginRequest() {
        super(RequestType.LOGIN);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
