package com.ricalope.tweeter.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ricalope.tweeter.entities.User;
import com.ricalope.tweeter.model.UserRequestDto;
import com.ricalope.tweeter.model.UserResponseDto;

@Mapper(componentModel="spring", uses = {CredentialsMapper.class, ProfileMapper.class})
public interface UserMapper {
	
	@Mapping(target="username", source="credentials.username")
    UserResponseDto entityToDto(User entity);
	
    User dtoToEntity(UserRequestDto userRequestDto);
    
    List<UserResponseDto> entitiesToDtos(List<User> users);
    
    List<User> dtosToEntities(List<UserRequestDto> userRequestDtos);

}
