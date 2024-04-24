package com.example.demo.weather.repo;

import com.example.demo.weather.entity.MidLandAreaCode;
import com.example.demo.weather.entity.MidTaAreaCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MidTaAreaCodeRepo extends JpaRepository<MidTaAreaCode, Long> {
    Optional<MidTaAreaCode> findByArea(String areaName);
}
