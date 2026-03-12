package com.app.publicvendorprofile.repository;

import com.app.publicvendorprofile.entity.VendorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorProfileRepository extends JpaRepository<VendorProfile, Long> {
}
