package com.app.publicvendorprofile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.publicvendorprofile.entity.SubService;

@Repository
public interface SubServiceRepository extends JpaRepository<SubService, Long> {
    List<SubService> findByServiceIdIn(List<Long> serviceIds);
    // Return active sub-services
    List<SubService> findByIsActiveTrue();
        List<SubService> findByServiceIdInAndIsActiveTrue(List<Long> serviceIds);
        List<SubService> findBySubServiceIdInAndIsActiveTrue(List<Long> subServiceIds);
}
