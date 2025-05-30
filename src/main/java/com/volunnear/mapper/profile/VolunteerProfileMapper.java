package com.volunnear.mapper.profile;

import com.volunnear.dto.request.profile.CreateVolunteerProfileInfoRequestDTO;
import com.volunnear.dto.response.profile.VolunteerProfileResponseDTO;
import com.volunnear.entity.profile.VolunteerProfile;
import com.volunnear.entity.users.AppUser;
import com.volunnear.mapper.user.AppUserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;


@Mapper(componentModel = "spring", imports = {LocalDate.class , AppUserMapper.class})
public interface VolunteerProfileMapper {
    VolunteerProfileMapper mapper = Mappers.getMapper(VolunteerProfileMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appUser", source = "appUser")
    VolunteerProfile toEntity(CreateVolunteerProfileInfoRequestDTO requestDTO, AppUser appUser);

    @Mapping(target = "email", source = "volunteerProfile.appUser.email")
    @Mapping(target = "username", source = "volunteerProfile.appUser.username")
    @Mapping(target = "created", source = "volunteerProfile.appUser.created")
    VolunteerProfileResponseDTO toDto(VolunteerProfile volunteerProfile);
}