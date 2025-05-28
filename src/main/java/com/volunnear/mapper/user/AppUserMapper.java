package com.volunnear.mapper.user;

import com.volunnear.dto.request.user.RegisterAppUserDTO;
import com.volunnear.entity.users.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.Set;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, Set.class})
public interface AppUserMapper {
    RegisterAppUserDTO toDto(AppUser appUser);

    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    @Mapping(target = "roles", expression = "java(Set.of(role))")
    @Mapping(target = "email", source = "registerAppUserDTO.email")
    AppUser toEntity(RegisterAppUserDTO registerAppUserDTO, String role);
}
