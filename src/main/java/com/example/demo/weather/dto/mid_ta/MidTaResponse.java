package com.example.demo.weather.dto.mid_ta;

import com.example.demo.weather.dto.mid_land.MidLandBody;
import com.example.demo.weather.dto.mid_land.MidLandHeader;
import lombok.Data;

@Data
public class MidTaResponse {
    private MidTaHeader header;
    private MidTaBody body;
}
