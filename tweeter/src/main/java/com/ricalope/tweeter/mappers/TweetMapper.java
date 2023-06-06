package com.ricalope.tweeter.mappers;

import java.util.List;
import java.util.SortedSet;

import org.mapstruct.Mapper;

import com.ricalope.tweeter.entities.Tweet;
import com.ricalope.tweeter.model.TweetRequestDto;
import com.ricalope.tweeter.model.TweetResponseDto;


@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TweetMapper {

	TweetResponseDto entityToDto(Tweet tweet);

	Tweet dtoToEntity(TweetRequestDto tweetRequestDto);

	List<TweetResponseDto> entitiesToDtos(List<Tweet> tweets);

	List<Tweet> dtosToEntities(List<TweetRequestDto> tweetRequestDtos);

	// added for context route
	List<TweetResponseDto> setEntitiesToDtos(SortedSet<Tweet> beforeSet);

}
