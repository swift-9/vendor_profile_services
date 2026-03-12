package com.app.publicvendorprofile.repository;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VendorWorkRepository {

    private static final Logger log = LoggerFactory.getLogger(VendorWorkRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public VendorWorkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Long> findServiceIdsByVendorId(Long vendorId) {
        // team schema uses service_category_id for the linked service id
        String sql = "SELECT DISTINCT service_category_id FROM vendor_work WHERE vendor_id = ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong(1), vendorId);
        } catch (DataAccessException dae) {
            log.warn("Failed to query service ids for vendor {}: {}", vendorId, dae.getMessage());
            return Collections.emptyList();
        }
    }
}
