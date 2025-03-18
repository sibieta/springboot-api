package com.sibieta.demo.model.dto;

import java.util.Date;
import java.util.List;

import com.sibieta.demo.model.Usuario;

import java.util.UUID;

public class UsuarioDTO {
    private UUID id;
    private String name;
    private String email;
    private Date created;
    private Date modified;
    private Date lastLogin;
    private boolean isActive;
    private List<PhoneDTO> phones;

    public UsuarioDTO(Usuario user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.created = user.getCreated();
        this.modified = user.getModified();
        this.lastLogin = user.getLastLogin();
        this.isActive = user.isActive();
        this.phones = user.getPhones().stream().map(phone -> new PhoneDTO(phone)).toList();
    }

    public UsuarioDTO() {
        //TODO Auto-generated constructor stub
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<PhoneDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDTO> phones) {
        this.phones = phones;
    }
    
    

}
