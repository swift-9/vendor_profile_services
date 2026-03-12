package com.app.publicvendorprofile.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.publicvendorprofile.dto.OfferedSubServiceDto;
import com.app.publicvendorprofile.dto.ReviewDto;
import com.app.publicvendorprofile.dto.VendorProfileDto;
import com.app.publicvendorprofile.service.VendorService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/public/vendors")
public class PublicVendorController {

    private final VendorService vendorService;

    public PublicVendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @Operation(summary = "Get vendor profile by id")
    @GetMapping("/{vendorId}/profile")
    public ResponseEntity<VendorProfileDto> getVendorProfile(@PathVariable("vendorId") Long vendorId) {
        VendorProfileDto profile = vendorService.getVendorProfile(vendorId);
        return ResponseEntity.ok(profile);
    }

    @Operation(summary = "Get vendor reviews (paged)")
    @GetMapping("/{vendorId}/reviews")
    public ResponseEntity<Page<ReviewDto>> getVendorReviews(@PathVariable("vendorId") Long vendorId,
                                                            Pageable pageable) {
        Page<ReviewDto> reviews = vendorService.getVendorReviews(vendorId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Get offered sub-services for a vendor (paged)")
    @GetMapping("/{vendorId}/offered-subservices")
    public ResponseEntity<Page<OfferedSubServiceDto>> getOfferedSubServices(@PathVariable("vendorId") Long vendorId,
                                                                            Pageable pageable) {
        Page<OfferedSubServiceDto> subs = vendorService.getOfferedSubServices(vendorId, pageable);
        return ResponseEntity.ok(subs);
    }

}
