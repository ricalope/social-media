package com.ricalope.tweeter.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ricalope.tweeter.model.CredentialsDto;
import com.ricalope.tweeter.model.TweetResponseDto;
import com.ricalope.tweeter.model.UserRequestDto;
import com.ricalope.tweeter.model.UserResponseDto;
import com.ricalope.tweeter.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@GetMapping
	public List<UserResponseDto> getAllUsers() {
		return userService.getAllUsers();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
		return userService.createUser(userRequestDto);
	}
	
	@GetMapping("/@{username}")
	public UserResponseDto getUserByUsername(@PathVariable String username) {
		return userService.getUserByUsername(username);
	}
	
	@PatchMapping("/@{username}")
	public UserResponseDto updateUser(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
		return userService.updateUser(username, userRequestDto);
	}
	
	@DeleteMapping("/@{username}")
	public UserResponseDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		return userService.deleteUser(username, credentialsDto);
	}
	
	@PostMapping("/@{username}/follow")
	@ResponseStatus(HttpStatus.CREATED)
	public void followUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		userService.followUser(username, credentialsDto);
	}
	
	@GetMapping("/@{username}/followers")
	public List<UserResponseDto> getFollowers(@PathVariable String username) {
		return userService.getFollowers(username);
	}
	
	@PostMapping("/@{username}/unfollow")
	@ResponseStatus(HttpStatus.CREATED)
	public void unFollowUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		userService.unFollowUser(username, credentialsDto);
	}
	
	@GetMapping("/@{username}/feed")
	public List<TweetResponseDto> getUserFeed(@PathVariable String username) {
		return userService.getUserFeed(username);
	}
	
	@GetMapping("/@{username}/following")
	public List<UserResponseDto> getFollowing(@PathVariable String username) {
		return userService.getFollowing(username);
	}
	
	@GetMapping("/@{username}/tweets")
	public List<TweetResponseDto> getUserTweets(@PathVariable String username) {
		return userService.getUserTweets(username);
	}
	
	@GetMapping("/@{username}/mentions")
	public List<TweetResponseDto> getUserMentions(@PathVariable String username) {
		return userService.getUserMentions(username);
	}

}
