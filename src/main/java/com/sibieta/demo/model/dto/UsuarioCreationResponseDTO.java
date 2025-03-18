package com.sibieta.demo.model.dto;

import java.util.Date;

import com.sibieta.demo.model.Usuario;

import java.util.UUID;

public class UsuarioCreationResponseDTO {
    private UUID id;
    private Date created;
    private Date modified;
    private Date lastLogin;
    private String token;
    private boolean isActive;


    public UsuarioCreationResponseDTO(Usuario user) {
        this.id = user.getId();
        this.created = user.getCreated();
        this.modified = user.getModified();
        this.lastLogin = user.getLastLogin();
        this.isActive = user.isActive();
    }

    
    public UsuarioCreationResponseDTO() {
        //TODO Auto-generated constructor stub
    }


    public void setId(UUID id) {
        this.id = id;
    }


    public void setCreated(Date created) {
        this.created = created;
    }


    public void setModified(Date modified) {
        this.modified = modified;
    }


    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }


    public void setToken(String token) {
        this.token = token;
    }


    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }


    public UUID getId() { return id; }
    public Date getCreated() { return created; }
    public Date getModified() { return modified; }
    public Date getLastLogin() { return lastLogin; }
    public String getToken() { return token; }
    public boolean isActive() { return isActive; }
}


