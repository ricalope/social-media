package com.ricalope.tweeter.services;

import java.util.List;

import com.ricalope.tweeter.model.HashtagDto;
import com.ricalope.tweeter.model.TweetResponseDto;

public interface HashtagService {

	List<HashtagDto> getAllHashtags();

	List<TweetResponseDto> getTaggedTweets(String label);

}
