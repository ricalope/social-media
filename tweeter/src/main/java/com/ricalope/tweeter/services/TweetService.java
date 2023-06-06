package com.ricalope.tweeter.services;

import java.util.List;

import com.ricalope.tweeter.model.ContextDto;
import com.ricalope.tweeter.model.CredentialsDto;
import com.ricalope.tweeter.model.HashtagDto;
import com.ricalope.tweeter.model.TweetRequestDto;
import com.ricalope.tweeter.model.TweetResponseDto;
import com.ricalope.tweeter.model.UserResponseDto;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

	TweetResponseDto getTweetById(Long id);

	TweetResponseDto deleteTweet(Long id, CredentialsDto credentialsDto);

	void addLikeToTweet(Long id, CredentialsDto credentialsDto);

	TweetResponseDto createReply(Long id, TweetRequestDto tweetRequestDto);

	TweetResponseDto createRepost(Long id, CredentialsDto credentialsDto);

	List<HashtagDto> getTweetTags(Long id);

	List<UserResponseDto> getAllTweetLikes(Long id);

	ContextDto getTweetContext(Long id);

	List<TweetResponseDto> getTweetReplies(Long id);

	List<TweetResponseDto> getTweetReposts(Long id);

	List<UserResponseDto> getTweetMentions(Long id);

}
