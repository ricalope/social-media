package com.ricalope.tweeter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ricalope.tweeter.entities.Profile;
import com.ricalope.tweeter.model.ProfileDto;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
	
	ProfileDto entityToDto(Profile profile);
	
	Profile dtoToEntity(ProfileDto profileDto);
	
    List<ProfileDto> entitiesToDtos(List<Profile> profiles);
    
    List<Profile> dtosToEntities(List<ProfileDto> profileDtos);

}
