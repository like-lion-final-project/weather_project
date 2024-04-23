package com.example.demo.weather.dto.mid_land;

import com.example.demo.weather.dto.srt_ncst.NcstItems;
import lombok.Data;

@Data
public class MidLandBody {
    private String dataType;
    private MidLandItems items;
}
