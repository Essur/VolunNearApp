package com.volunnear.service;

import com.volunnear.dto.geoInfo.AddressDTO;
import com.volunnear.dto.geoInfo.GeoCodingResponse;
import com.volunnear.exception.GeoCodingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoService {
    private final WebClient webClient;
    private final GeometryFactory geometryFactory;

    public Point geocode(AddressDTO addressDTO) {
        GeoCodingResponse[] response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", addressDTO.toQuery())
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .bodyToMono(GeoCodingResponse[].class)
                .timeout(Duration.ofSeconds(3))
                .blockOptional()
                .orElseThrow(() -> new GeoCodingException("No response from geocoding service"));

        if (response.length == 0) {
            throw new GeoCodingException("No location found for: " + addressDTO.toQuery());
        }

        double lat = Double.parseDouble(response[0].lat());
        double lon = Double.parseDouble(response[0].lon());

        return geometryFactory.createPoint(new Coordinate(lon, lat));
    }
}
