package com.ricalope.tweeter.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ricalope.tweeter.entities.Credentials;
import com.ricalope.tweeter.entities.Profile;
import com.ricalope.tweeter.entities.Tweet;
import com.ricalope.tweeter.entities.User;
import com.ricalope.tweeter.exceptions.BadRequestException;
import com.ricalope.tweeter.exceptions.NotAuthorizedException;
import com.ricalope.tweeter.exceptions.NotFoundException;
import com.ricalope.tweeter.mappers.CredentialsMapper;
import com.ricalope.tweeter.mappers.ProfileMapper;
import com.ricalope.tweeter.mappers.TweetMapper;
import com.ricalope.tweeter.mappers.UserMapper;
import com.ricalope.tweeter.model.CredentialsDto;
import com.ricalope.tweeter.model.TweetResponseDto;
import com.ricalope.tweeter.model.UserRequestDto;
import com.ricalope.tweeter.model.UserResponseDto;
import com.ricalope.tweeter.repositories.UserRepository;
import com.ricalope.tweeter.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	private final UserMapper userMapper;
	private final ProfileMapper profileMapper;
	private final CredentialsMapper credentialsMapper;
	private final TweetMapper tweetMapper;
	
	private User getUser(String username) {
		Optional<User> optionalUser = userRepository.findTopByCredentialsUsername(username);
		if(optionalUser.isEmpty()) {
			throw new NotFoundException("The specified user could not be found.");
		}
		User user = optionalUser.get();
		return user;
	}
	
	private void validateUserCredentials(User userOne, User userTwo) {
		if(!userOne.getCredentials().getUsername().equals(userTwo.getCredentials().getUsername()) ||
			!userOne.getCredentials().getPassword().equals(userTwo.getCredentials().getPassword())) {
			throw new NotAuthorizedException("The supplied credentials do not match those of the specified user.");
		}
	}
	
	private void checkUser(User user) {
		if(user.isDeleted()) {
			throw new BadRequestException("The specified user has been deleted.");
		}
	}
	
	private List<Tweet> getNonDeletedTweets(User user) {
		List<Tweet> userTweets = user.getTweets()
				.stream().filter(tweet -> !tweet.isDeleted())
				.collect(Collectors.toList());
		return userTweets;
	}
	
	@Override
	public List<UserResponseDto> getAllUsers() {
		List<User> allUsers = userRepository.findAll()
				.stream().filter(user -> !user.isDeleted())
				.collect(Collectors.toList());
		
		return userMapper.entitiesToDtos(allUsers);
	}
	
	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {
		
		if(userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
			throw new BadRequestException("A username, password and at least an email are required to create an account");
		}
		
		Credentials credentials = credentialsMapper.dtoToEntity(userRequestDto.getCredentials());
		Profile profile = profileMapper.dtoToEntity(userRequestDto.getProfile());
		if(credentials.getPassword() == null || credentials.getUsername() == null || profile.getEmail() == null) {
			throw new BadRequestException("The username, password and email are required to create an account.");
		}
		Optional<User> existingUser = userRepository.findByCredentialsUsername(credentials.getUsername());
		
		if(existingUser.isPresent()) {
			if(!existingUser.get().isDeleted()) {
				throw new BadRequestException("The requested username already exists please choose another one.");
			}
			existingUser.get().setDeleted(false);
			existingUser.get().setCredentials(credentials);
			existingUser.get().setProfile(profile);
			userRepository.saveAndFlush(existingUser.get());
			return userMapper.entityToDto(existingUser.get());
		}
		User userToSave = userMapper.dtoToEntity(userRequestDto);
		userRepository.saveAndFlush(userToSave);
		userToSave.setCredentials(credentials);
		userToSave.setProfile(profile);
		return userMapper.entityToDto(userToSave);
	}
	
	@Override
	public UserResponseDto getUserByUsername(String username) {
		User queriedUser = getUser(username);
		checkUser(queriedUser);
		return userMapper.entityToDto(queriedUser);
	}
	
	@Override
	public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
		if(userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
			throw new BadRequestException("Must provide the necessary credentials and profile update");
		}
		User updatedUserInfo = userMapper.dtoToEntity(userRequestDto);
		Profile profile = profileMapper.dtoToEntity(userRequestDto.getProfile());
		User userToUpdate = getUser(username);
		checkUser(userToUpdate);
		validateUserCredentials(userToUpdate, updatedUserInfo);
		updatedUserInfo.setProfile(profile);
		return userMapper.entityToDto(userRepository.saveAndFlush(userToUpdate));
	}
	
	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
		User userToDelete = getUser(username);
		User userCredentials = getUser(credentialsDto.getUsername());
		checkUser(userToDelete);
		validateUserCredentials(userToDelete, userCredentials);
		userToDelete.setDeleted(true);
		userRepository.saveAndFlush(userToDelete);
		return userMapper.entityToDto(userToDelete);
	}
	
	@Override
	public void followUser(String username, CredentialsDto credentialsDto) {
		if(credentialsDto.getPassword() == null || credentialsDto.getUsername() == null) {
			throw new BadRequestException("You must supply a username and password to follow a user");
		}
		User userToFollow = getUser(username);
		User loggedInUser = getUser(credentialsDto.getUsername());
		checkUser(userToFollow);
		if(userToFollow.getFollowers().contains(loggedInUser)) {
			throw new BadRequestException("You are already following this user");
		}
		userToFollow.getFollowers().add(loggedInUser);
		loggedInUser.getFollowing().add(userToFollow);
		userRepository.saveAndFlush(userToFollow);
		userRepository.saveAndFlush(loggedInUser);
	}
	
	@Override
	public List<UserResponseDto> getFollowers(String username) {
		User queriedUser = getUser(username);
		checkUser(queriedUser);
		List<User> userFollowers = queriedUser.getFollowers()
				.stream().filter(user -> !user.isDeleted())
				.collect(Collectors.toList());
		return userMapper.entitiesToDtos(userFollowers);
	}
	
	@Override
	public void unFollowUser(String username, CredentialsDto credentialsDto) {
		if(credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
			throw new BadRequestException("You must supply your username and password to unfollow this user");
		}
		User userToUnfollow = getUser(username);
		User loggedInUser = getUser(credentialsDto.getUsername());
		checkUser(userToUnfollow);
		if(!userToUnfollow.getFollowers().contains(loggedInUser)) {
			throw new BadRequestException("You are not currently following this user therefore you cannot unfollow");
		}
		userToUnfollow.getFollowers().remove(loggedInUser);
		loggedInUser.getFollowing().remove(userToUnfollow);
		userRepository.saveAndFlush(userToUnfollow);
		userRepository.saveAndFlush(loggedInUser);
	}
	
	@Override
	public List<TweetResponseDto> getUserFeed(String username) {
		User queriedUser = getUser(username);
		List<Tweet> queriedUserTweets = getNonDeletedTweets(queriedUser);
		for(User user : queriedUser.getFollowing()) {
			for(Tweet tweet : user.getTweets()) {
				if(!tweet.isDeleted()) {
					queriedUserTweets.add(tweet);
				}
			}
		}
		return tweetMapper.entitiesToDtos(queriedUserTweets);
	}
	
	@Override
	public List<UserResponseDto> getFollowing(String username) {
		User queriedUser = getUser(username);
		checkUser(queriedUser);
		List<User> userFollowing = queriedUser.getFollowing()
				.stream().filter(user -> !user.isDeleted())
				.collect(Collectors.toList());
		return userMapper.entitiesToDtos(userFollowing);
	}
	
	@Override
	public List<TweetResponseDto> getUserTweets(String username) {
		User queriedUser = getUser(username);
		List<Tweet> userTweets = getNonDeletedTweets(queriedUser);
		return tweetMapper.entitiesToDtos(userTweets);
	}
	
	@Override
	public List<TweetResponseDto> getUserMentions(String username) {
		User queriedUser = getUser(username);
		List<Tweet> userMentions = queriedUser.getUserMentions()
				.stream().filter(tweet -> !tweet.isDeleted())
				.collect(Collectors.toList());
		
		return tweetMapper.entitiesToDtos(userMentions);
	}

}
