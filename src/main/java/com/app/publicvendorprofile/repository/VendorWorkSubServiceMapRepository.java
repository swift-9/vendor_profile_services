package com.app.publicvendorprofile.repository;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VendorWorkSubServiceMapRepository {

    private static final Logger log = LoggerFactory.getLogger(VendorWorkSubServiceMapRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public VendorWorkSubServiceMapRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Long> findSubServiceIdsByVendorId(Long vendorId) {
        // mapping table stores vendor_work_id; join to vendor_work to filter by vendor_id
        String sql = "SELECT DISTINCT vsm.sub_service_id FROM vendor_work_sub_service_map vsm "
                   + "JOIN vendor_work vw ON vsm.vendor_work_id = vw.vendor_work_id "
                   + "WHERE vw.vendor_id = ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong(1), vendorId);
        } catch (DataAccessException dae) {
            log.warn("Failed to query sub-service ids for vendor {}: {}", vendorId, dae.getMessage());
            return Collections.emptyList();
        }
    }
}
