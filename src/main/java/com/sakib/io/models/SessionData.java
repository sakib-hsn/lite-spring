package com.sakib.io.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionData {
    private String id;
    private long createdTime;
    private long lastLoginTime;
    private String username;
    private String ipAddress;
    private String country;
}
