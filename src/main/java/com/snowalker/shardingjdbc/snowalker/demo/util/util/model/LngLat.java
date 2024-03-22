package com.snowalker.shardingjdbc.snowalker.demo.util.util.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class LngLat implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal lng;//经度
    private BigDecimal lat;//纬度

    public LngLat() {
    }

    public LngLat(String location) {
        if (location != null && location.contains(",")) {
            String[] split = location.split(",");
            lng = new BigDecimal(split[0]);
            lat = new BigDecimal(split[1]);
        }
    }

    public LngLat(BigDecimal lng, BigDecimal lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }
    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }
    public BigDecimal getLat() {
        return lat;
    }
    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

}
