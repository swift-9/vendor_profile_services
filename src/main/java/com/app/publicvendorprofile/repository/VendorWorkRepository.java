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

    public java.util.List<com.app.publicvendorprofile.dto.VendorWorkDto> findWorksByVendorId(Long vendorId) {
        String sql = "SELECT vendor_work_id, vendor_id, work_type, agency_name, experience_years, service_category_id FROM vendor_work WHERE vendor_id = ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                com.app.publicvendorprofile.dto.VendorWorkDto dto = new com.app.publicvendorprofile.dto.VendorWorkDto();
                dto.setVendorWorkId(rs.getLong("vendor_work_id"));
                dto.setVendorId(rs.getLong("vendor_id"));
                dto.setWorkType(rs.getString("work_type"));
                dto.setAgencyName(rs.getString("agency_name"));
                int yrs = rs.getInt("experience_years");
                if (!rs.wasNull()) dto.setExperienceYears(yrs);
                long scid = rs.getLong("service_category_id");
                if (!rs.wasNull()) dto.setServiceCategoryId(scid);
                return dto;
            }, vendorId);
        } catch (DataAccessException dae) {
            log.warn("Failed to query works for vendor {}: {}", vendorId, dae.getMessage());
            return java.util.Collections.emptyList();
        }
    }
}
