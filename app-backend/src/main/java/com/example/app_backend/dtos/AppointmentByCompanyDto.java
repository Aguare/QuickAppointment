package com.example.app_backend.dtos;

public class AppointmentByCompanyDto {

    private String company;
    private Integer total;

    public AppointmentByCompanyDto(String company, Integer total) {
        this.company = company;
        this.total = total;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
