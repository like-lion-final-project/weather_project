package com.example.demo.weather.dto.mid_land;

import com.example.demo.weather.dto.srt_ncst.NcstBody;
import com.example.demo.weather.dto.srt_ncst.NcstHeader;
import lombok.Data;

@Data
public class MidLandResponse {
    private MidLandHeader header;
    private MidLandBody body;
}
