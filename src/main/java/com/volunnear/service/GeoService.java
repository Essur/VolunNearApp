package com.volunnear.service;

import com.volunnear.client.NominatimClient;
import com.volunnear.dto.geoInfo.AddressDTO;
import com.volunnear.dto.geoInfo.GeoCodingResponse;
import com.volunnear.exception.GeoCodingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class GeoService {
    private final NominatimClient nominatimClient;
    private final GeometryFactory geometryFactory;

    public Point geocode(AddressDTO addressDTO) {
        List<GeoCodingResponse> responses = nominatimClient.geocode(
                addressDTO.toQuery(),
                "json",
                1
        );

        if (responses == null || responses.isEmpty()) {
            throw new GeoCodingException("No location found for: " + addressDTO.toQuery());
        }

        GeoCodingResponse response = responses.getFirst();
        double lat = Double.parseDouble(response.lat());
        double lon = Double.parseDouble(response.lon());

        return geometryFactory.createPoint(new Coordinate(lon, lat));
    }
}
