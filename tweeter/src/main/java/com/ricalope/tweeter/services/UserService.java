package com.ricalope.tweeter.services;

import java.util.List;

import com.ricalope.tweeter.model.CredentialsDto;
import com.ricalope.tweeter.model.TweetResponseDto;
import com.ricalope.tweeter.model.UserRequestDto;
import com.ricalope.tweeter.model.UserResponseDto;

public interface UserService {

	List<UserResponseDto> getAllUsers();

	UserResponseDto createUser(UserRequestDto userRequestDto);

	UserResponseDto getUserByUsername(String username);

	UserResponseDto updateUser(String username, UserRequestDto userRequestDto);

	UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

	void followUser(String username, CredentialsDto credentialsDto);

	List<UserResponseDto> getFollowers(String username);

	void unFollowUser(String username, CredentialsDto credentialsDto);

	List<TweetResponseDto> getUserFeed(String username);

	List<UserResponseDto> getFollowing(String username);

	List<TweetResponseDto> getUserTweets(String username);

	List<TweetResponseDto> getUserMentions(String username);

}
