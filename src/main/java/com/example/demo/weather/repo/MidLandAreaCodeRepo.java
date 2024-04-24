package com.example.demo.weather.repo;

import com.example.demo.weather.entity.MidLandAreaCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MidLandAreaCodeRepo extends JpaRepository<MidLandAreaCode, Long> {
    Optional<MidLandAreaCode> findByArea(String areaName);
}
