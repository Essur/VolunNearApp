package com.volunnear.mapper.profile;

import com.volunnear.dto.request.profile.VolunteerProfileSaveRequestDTO;
import com.volunnear.dto.response.profile.VolunteerProfileResponseDTO;
import com.volunnear.entity.profile.VolunteerProfile;
import com.volunnear.entity.users.AppUser;
import com.volunnear.mapper.user.AppUserMapper;
import org.mapstruct.*;

import java.time.LocalDate;


@Mapper(componentModel = "spring", imports = {LocalDate.class, AppUserMapper.class})
public interface VolunteerProfileMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appUser", source = "appUser")
    VolunteerProfile toEntity(VolunteerProfileSaveRequestDTO dto, AppUser appUser);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(VolunteerProfileSaveRequestDTO dto, @MappingTarget VolunteerProfile entity);

    @Mapping(target = "email", source = "volunteerProfile.appUser.email")
    @Mapping(target = "username", source = "volunteerProfile.appUser.username")
    @Mapping(target = "created", source = "volunteerProfile.appUser.created")
    VolunteerProfileResponseDTO toDto(VolunteerProfile volunteerProfile);
}