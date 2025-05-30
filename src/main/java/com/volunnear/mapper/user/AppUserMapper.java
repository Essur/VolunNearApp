package com.volunnear.mapper.user;

import com.volunnear.dto.request.user.RegisterAppUserDTO;
import com.volunnear.entity.users.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Set;

@Mapper(componentModel = "spring", imports = {LocalDate.class, Set.class})
public interface AppUserMapper {
    AppUserMapper mapper = Mappers.getMapper(AppUserMapper.class);

    RegisterAppUserDTO toDto(AppUser appUser);

    @Mapping(target = "created", expression = "java(LocalDate.now())")
    @Mapping(target = "roles", expression = "java(Set.of(role))")
    @Mapping(target = "email", source = "registerAppUserDTO.email")
    AppUser toEntity(RegisterAppUserDTO registerAppUserDTO, String role);
}
