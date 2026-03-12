package com.app.publicvendorprofile.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.publicvendorprofile.dto.OfferedSubServiceDto;
import com.app.publicvendorprofile.dto.ReviewDto;
import com.app.publicvendorprofile.dto.VendorProfileDto;
import com.app.publicvendorprofile.entity.Review;
import com.app.publicvendorprofile.entity.SubService;
import com.app.publicvendorprofile.entity.VendorProfile;
import com.app.publicvendorprofile.repository.ReviewRepository;
import com.app.publicvendorprofile.repository.SubServiceRepository;
import com.app.publicvendorprofile.repository.VendorProfileRepository;
import com.app.publicvendorprofile.repository.VendorWorkRepository;
import com.app.publicvendorprofile.repository.VendorWorkSubServiceMapRepository;
import com.app.publicvendorprofile.service.CartClient;
import com.app.publicvendorprofile.service.VendorService;

import reactor.core.publisher.Mono;

@Service
public class VendorServiceImpl implements VendorService {

    private final CartClient cartClient;
    private final VendorProfileRepository vendorRepo;
    private final ReviewRepository reviewRepo;
    private final SubServiceRepository subServiceRepo;
    private final VendorWorkRepository vendorWorkRepo;
    private final VendorWorkSubServiceMapRepository vendorWorkSubServiceMapRepo;

    public VendorServiceImpl(CartClient cartClient,
                             VendorProfileRepository vendorRepo,
                             ReviewRepository reviewRepo,
                             SubServiceRepository subServiceRepo,
                             VendorWorkRepository vendorWorkRepo,
                             VendorWorkSubServiceMapRepository vendorWorkSubServiceMapRepo) {
        this.cartClient = cartClient;
        this.vendorRepo = vendorRepo;
        this.reviewRepo = reviewRepo;
        this.subServiceRepo = subServiceRepo;
        this.vendorWorkRepo = vendorWorkRepo;
        this.vendorWorkSubServiceMapRepo = vendorWorkSubServiceMapRepo;
    }

    @Override
    public VendorProfileDto getVendorProfile(Long vendorId) {
        Objects.requireNonNull(vendorId, "vendorId must not be null");
        VendorProfile vp = vendorRepo.findById(vendorId)
                .orElseThrow(() -> new com.app.publicvendorprofile.exception.ResourceNotFoundException("Vendor not found: " + vendorId));
        VendorProfileDto dto = new VendorProfileDto();
        dto.setVendorId(vp.getVendorId());
        dto.setFullName(vp.getFullName());
        dto.setPhoneNumber(vp.getPhoneNumber());
        dto.setAge(vp.getAge());
        dto.setGender(vp.getGender());
        dto.setAddressLine(vp.getAddressLine());
        dto.setCity(vp.getCity());
        dto.setState(vp.getState());
        dto.setCountry(vp.getCountry());
        dto.setPincode(vp.getPincode());
        dto.setPhotoPath(vp.getPhotoPath());
        dto.setIsVerified(vp.getIsVerified());
        return dto;
    }

    @Override
    public Page<ReviewDto> getVendorReviews(Long vendorId, Pageable pageable) {
        Objects.requireNonNull(vendorId, "vendorId must not be null");
        Page<Review> page = reviewRepo.findByVendorId(vendorId, pageable);
        // fallback to native query if JPA query returned empty but records may exist with snake_case columns
        if ((page == null || page.getTotalElements() == 0) && reviewRepo != null) {
            try {
                Page<Review> nativePage = reviewRepo.findByVendorIdNative(vendorId, pageable);
                if (nativePage != null && nativePage.getTotalElements() > 0) {
                    page = nativePage;
                }
            } catch (Exception ex) {
                // ignore and proceed with original page
            }
        }
        if (page == null) {
            page = new PageImpl<>(new java.util.ArrayList<>(java.util.Collections.<Review>emptyList()), java.util.Objects.requireNonNull(pageable, "pageable must not be null"), 0);
        }
        List<ReviewDto> content = page.getContent().stream().map(this::toDto).collect(Collectors.toList());
        Objects.requireNonNull(content, "content must not be null");
        Objects.requireNonNull(pageable, "pageable must not be null");
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    @Override
    public List<OfferedSubServiceDto> getOfferedSubServices(Long vendorId) {
        // keep legacy behavior by returning unpaged content
        return getOfferedSubServices(vendorId, Pageable.unpaged()).getContent();
    }

    @Override
    public org.springframework.data.domain.Page<OfferedSubServiceDto> getOfferedSubServices(Long vendorId, Pageable pageable) {
        Objects.requireNonNull(vendorId, "vendorId must not be null");

        if (!vendorRepo.existsById(vendorId)) {
            throw new com.app.publicvendorprofile.exception.ResourceNotFoundException("Vendor not found: " + vendorId);
        }

        List<Long> vendorSubIds = vendorWorkSubServiceMapRepo.findSubServiceIdsByVendorId(vendorId);
        List<Long> serviceIds = vendorWorkRepo.findServiceIdsByVendorId(vendorId);

        List<SubService> serviceSubs = (serviceIds == null || serviceIds.isEmpty())
                ? java.util.Collections.emptyList()
                : subServiceRepo.findByServiceIdInAndIsActiveTrue(serviceIds);

        List<SubService> vendorSpecificSubs = (vendorSubIds == null || vendorSubIds.isEmpty())
                ? java.util.Collections.emptyList()
                : subServiceRepo.findBySubServiceIdInAndIsActiveTrue(vendorSubIds);

        // combine and dedupe
        java.util.Map<Long, SubService> combined = new java.util.LinkedHashMap<>();
        for (SubService s : serviceSubs) {
            if (s != null && s.getSubServiceId() != null) combined.put(s.getSubServiceId(), s);
        }
        for (SubService s : vendorSpecificSubs) {
            if (s != null && s.getSubServiceId() != null) combined.put(s.getSubServiceId(), s);
        }

        List<OfferedSubServiceDto> dtos = combined.values().stream().map(s -> {
            OfferedSubServiceDto dto = new OfferedSubServiceDto();
            dto.setSubServiceId(s.getSubServiceId());
            dto.setServiceId(s.getServiceId());
            dto.setSubServiceName(s.getSubServiceName());
            dto.setDescription(s.getDescription());
            dto.setDuration(s.getDuration());
            dto.setIsActive(Boolean.TRUE.equals(s.getIsActive()));
            dto.setPrice(s.getPrice() != null ? s.getPrice().doubleValue() : null);
            return dto;
        }).collect(Collectors.toList());
        dtos = java.util.List.copyOf(java.util.Objects.requireNonNull(dtos, "dtos must not be null"));

        if (pageable == null || pageable.isUnpaged()) {
            List<OfferedSubServiceDto> unpagedList = new java.util.ArrayList<>(dtos);
            return new PageImpl<>(unpagedList);
        }
        int total = dtos.size();
        long offset = pageable.getOffset();
        if (offset >= total) {
            List<OfferedSubServiceDto> emptyList = new java.util.ArrayList<>();
            return new PageImpl<>(emptyList, pageable, total);
        }
        int fromIndex = (int) offset;
        int toIndex = Math.min(fromIndex + pageable.getPageSize(), total);
        List<OfferedSubServiceDto> pageContent = new java.util.ArrayList<>(dtos.subList(fromIndex, toIndex));
        return new PageImpl<>(pageContent, pageable, total);
    }

    @Override
    public Map<String, Object> addToCart(Map<String, Object> addCartRequest) {
        Mono<Map<String, Object>> responseMono = cartClient.addItemToCart(addCartRequest);
        return responseMono.block();
    }

    private ReviewDto toDto(Review r) {
        ReviewDto dto = new ReviewDto();
        dto.setReviewId(r.getReviewId());
        dto.setUserId(r.getUserId());
        dto.setVendorId(r.getVendorId());
        dto.setBookingId(r.getBookingId());
        dto.setRating(r.getRating());
        dto.setComments(r.getComments());
        dto.setReviewDate(r.getReviewDate());
        dto.setReviewTime(r.getReviewTime());
        dto.setStatus(r.getStatus());
        return dto;
    }
}
