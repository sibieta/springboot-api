package com.sibieta.demo.model.dto;

import com.sibieta.demo.model.Phone;

public class PhoneDTO {
    private Long id;
    private String number;
    private String citycode;
    private String contrycode;

    public PhoneDTO(Phone phone) {
        this.id = phone.getId();
        this.number = phone.getNumber();
        this.citycode = phone.getCitycode();
        this.contrycode = phone.getContrycode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getContrycode() {
        return contrycode;
    }

    public void setContrycode(String contrycode) {
        this.contrycode = contrycode;
    }

    

}
