package com.ricalope.tweeter.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ricalope.tweeter.entities.Hashtag;
import com.ricalope.tweeter.entities.Tweet;
import com.ricalope.tweeter.exceptions.NotFoundException;
import com.ricalope.tweeter.mappers.HashtagMapper;
import com.ricalope.tweeter.mappers.TweetMapper;
import com.ricalope.tweeter.model.HashtagDto;
import com.ricalope.tweeter.model.TweetResponseDto;
import com.ricalope.tweeter.repositories.HashtagRepository;
import com.ricalope.tweeter.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
	
	private final HashtagRepository hashtagRepository;
	
	private final HashtagMapper hashtagMapper;
	private final TweetMapper tweetMapper;
	
	private Hashtag getHashtag(String label) {
		Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label); 
		if(optionalHashtag.isEmpty()) {
			throw new NotFoundException("The specified hashtag could not be found");
		}
		return optionalHashtag.get();
	}
	
	@Override
	public List<HashtagDto> getAllHashtags() {
		List<Hashtag> allHashtags = hashtagRepository.findAll();
		return hashtagMapper.entitiesToDtos(allHashtags);
	}
	
	@Override
	public List<TweetResponseDto> getTaggedTweets(String label) {
		Hashtag queriedHashtag = getHashtag(label);
		List<Tweet> taggedTweets = queriedHashtag.getHashtaggedTweets();
		return tweetMapper.entitiesToDtos(taggedTweets);
	}

}
