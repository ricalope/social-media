package com.ricalope.tweeter.mappers;

import org.mapstruct.Mapper;

import com.ricalope.tweeter.entities.Credentials;
import com.ricalope.tweeter.model.CredentialsDto;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

	CredentialsDto entityToDto(Credentials credentials);
	
	Credentials dtoToEntity(CredentialsDto credentialsDto);
	
}
