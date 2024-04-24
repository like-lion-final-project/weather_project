package com.example.demo.weather.service.fcst;

import com.example.demo.weather.dto.MidLandDto;
import com.example.demo.weather.dto.MidTaDto;
import com.example.demo.weather.entity.MidLandAreaCode;
import com.example.demo.weather.entity.MidTaAreaCode;
import com.example.demo.weather.repo.MidLandAreaCodeRepo;
import com.example.demo.weather.repo.MidTaAreaCodeRepo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MidAreaCodeService {
    private final MidLandAreaCodeRepo midLandAreaCodeRepo;
    private final MidTaAreaCodeRepo midTaAreaCodeRepo;

    public MidLandDto readLandAreaCode(String area) {
        Optional<MidLandAreaCode> midLandAreaCodeOptional = midLandAreaCodeRepo.findByAreaContaining(area);
        if (midLandAreaCodeOptional.isPresent()) {
            return MidLandDto.fromEntity(midLandAreaCodeOptional.get());
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public MidTaDto readTaAreaCode(String area) {
        Optional<MidTaAreaCode> midTaAreaCodeOptional = midTaAreaCodeRepo.findByArea(area);
        if (midTaAreaCodeOptional.isPresent()) {
            return MidTaDto.fromEntity(midTaAreaCodeOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
