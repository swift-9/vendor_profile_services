package com.app.publicvendorprofile.dto;

public class VendorWorkDto {
    private Long vendorWorkId;
    private Long vendorId;
    private String workType;
    private String agencyName;
    private Integer experienceYears;
    private Long serviceCategoryId;

    public Long getVendorWorkId() {
        return vendorWorkId;
    }

    public void setVendorWorkId(Long vendorWorkId) {
        this.vendorWorkId = vendorWorkId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Long getServiceCategoryId() {
        return serviceCategoryId;
    }

    public void setServiceCategoryId(Long serviceCategoryId) {
        this.serviceCategoryId = serviceCategoryId;
    }
}
