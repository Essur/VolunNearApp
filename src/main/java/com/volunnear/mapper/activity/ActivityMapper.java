package com.volunnear.mapper.activity;

import com.volunnear.dto.request.activity.ActivitySaveRequestDTO;
import com.volunnear.dto.response.activity.ActivityResponseDTO;
import com.volunnear.entity.activity.Activity;
import com.volunnear.entity.profile.OrganizationProfile;
import com.volunnear.mapper.profile.OrganizationProfileMapper;
import com.volunnear.mapper.user.AppUserMapper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = { OrganizationProfileMapper.class, AppUserMapper.class, LocalDateTime.class, GeometryFactory.class})
public interface ActivityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "startedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "organizationProfile", source = "organizationProfile")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "location", source = "dto", qualifiedByName = "toPoint" )
    Activity toEntity(ActivitySaveRequestDTO dto, OrganizationProfile organizationProfile, @Context GeometryFactory geometryFactory);

    @Mapping(target = "location", source = "dto", qualifiedByName = "toPoint" )
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ActivitySaveRequestDTO dto,  @MappingTarget Activity activity, @Context GeometryFactory geometryFactory);

    @Named("toPoint")
    default Point toPoint(ActivitySaveRequestDTO requestDTO, @Context GeometryFactory geometryFactory) {
        return geometryFactory.createPoint(new Coordinate(requestDTO.getLng(), requestDTO.getLat()));
    }

    @Mapping(target = "organizationId", source = "activity.organizationProfile.id")
    ActivityResponseDTO toDto(Activity activity);
}
