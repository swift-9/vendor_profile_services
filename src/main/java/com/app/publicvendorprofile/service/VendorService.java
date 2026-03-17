package com.app.publicvendorprofile.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.publicvendorprofile.dto.OfferedSubServiceDto;
import com.app.publicvendorprofile.dto.ReviewDto;
import com.app.publicvendorprofile.dto.VendorProfileDto;

public interface VendorService {

    VendorProfileDto getVendorProfile(Long vendorId);

    Page<ReviewDto> getVendorReviews(Long vendorId, Pageable pageable);

    List<OfferedSubServiceDto> getOfferedSubServices(Long vendorId);

    // paged variant for API
    org.springframework.data.domain.Page<OfferedSubServiceDto> getOfferedSubServices(Long vendorId, org.springframework.data.domain.Pageable pageable);

}
