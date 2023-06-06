package com.ricalope.tweeter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ricalope.tweeter.entities.Hashtag;
import com.ricalope.tweeter.model.HashtagDto;

@Mapper(componentModel = "spring")
public interface HashtagMapper {
	
	HashtagDto entityToDto(Hashtag hashtag);
	
	Hashtag dtoToEntity(HashtagDto hashtagDto);
	
	List<HashtagDto> entitiesToDtos(List<Hashtag> hashtags);
	
	List<Hashtag> dtosToEntities(List<HashtagDto> hashtagDtos);

}
