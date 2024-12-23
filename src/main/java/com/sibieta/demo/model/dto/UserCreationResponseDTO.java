package com.sibieta.demo.model.dto;

import java.util.Date;

import com.sibieta.demo.model.User;

public class UserCreationResponseDTO {
    private Long id;
    private Date created;
    private Date modified;
    private Date lastLogin;
    private String token;
    private boolean isActive;


    public UserCreationResponseDTO(User user) {
        this.id = user.getId();
        this.created = user.getCreated();
        this.modified = user.getModified();
        this.lastLogin = user.getLastLogin();
        this.token = user.getToken();
        this.isActive = user.isActive();
    }


    public Long getId() { return id; }
    public Date getCreated() { return created; }
    public Date getModified() { return modified; }
    public Date getLastLogin() { return lastLogin; }
    public String getToken() { return token; }
    public boolean isActive() { return isActive; }
}


