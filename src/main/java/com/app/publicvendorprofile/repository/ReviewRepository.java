package com.app.publicvendorprofile.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.publicvendorprofile.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByVendorId(Long vendorId, Pageable pageable);

    // Native query fallback that checks both camelCase and snake_case column names
    @Query(value = "SELECT r.review_id AS review_id, "
         + "COALESCE(r.userId, r.user_id) AS userId, "
         + "COALESCE(r.vendorId, r.vendor_id) AS vendorId, "
         + "COALESCE(r.bookingId, r.booking_id) AS bookingId, "
         + "r.rating AS rating, r.comments AS comments, r.review_date AS review_date, r.review_time AS review_time, r.status AS status "
         + "FROM reviews r WHERE COALESCE(r.vendorId, r.vendor_id) = :vendorId",
        countQuery = "SELECT COUNT(*) FROM reviews r WHERE COALESCE(r.vendorId, r.vendor_id) = :vendorId",
        nativeQuery = true)
    Page<Review> findByVendorIdNative(@Param("vendorId") Long vendorId, Pageable pageable);
}
