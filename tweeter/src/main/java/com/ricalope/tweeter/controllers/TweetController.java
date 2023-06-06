package com.ricalope.tweeter.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ricalope.tweeter.model.ContextDto;
import com.ricalope.tweeter.model.CredentialsDto;
import com.ricalope.tweeter.model.HashtagDto;
import com.ricalope.tweeter.model.TweetRequestDto;
import com.ricalope.tweeter.model.TweetResponseDto;
import com.ricalope.tweeter.model.UserResponseDto;
import com.ricalope.tweeter.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetController {
	
	private final TweetService tweetService;
	
	@GetMapping
	public List<TweetResponseDto> getAllTweets() {
		return tweetService.getAllTweets();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createTweet(tweetRequestDto);
	}
	
	@GetMapping("/{id}")
	public TweetResponseDto getTweetById(@PathVariable Long id) {
		return tweetService.getTweetById(id);
	}
	
	@DeleteMapping("/{id}")
	public TweetResponseDto deleteTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.deleteTweet(id, credentialsDto);
	}
	
	@PostMapping("/{id}/like")
	@ResponseStatus(HttpStatus.CREATED)
	public void addLikeToTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		tweetService.addLikeToTweet(id, credentialsDto);
	}
	
	@PostMapping("/{id}/reply")
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto createReply(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createReply(id, tweetRequestDto);
	}
	
	@PostMapping("/{id}/repost")
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto createRepost(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.createRepost(id, credentialsDto);
	}
	
	@GetMapping("/{id}/tags")
	public List<HashtagDto> getTweetTags(@PathVariable Long id) {
		return tweetService.getTweetTags(id);
	}
	
	@GetMapping("/{id}/likes")
	public List<UserResponseDto> getAllTweetLikes(@PathVariable Long id) {
		return tweetService.getAllTweetLikes(id);
	}
	
	@GetMapping("/{id}/context")
	public ContextDto getTweetContext(@PathVariable Long id) {
		return tweetService.getTweetContext(id);
	}
	
	@GetMapping("/{id}/replies")
	public List<TweetResponseDto> getTweetReplies(@PathVariable Long id) {
		return tweetService.getTweetReplies(id);
	}
	
	@GetMapping("/{id}/reposts")
	public List<TweetResponseDto> getTweetReposts(@PathVariable Long id) {
		return tweetService.getTweetReposts(id);
	}
	
	@GetMapping("/{id}/mentions")
	public List<UserResponseDto> getTweetMentions(@PathVariable Long id) {
		return tweetService.getTweetMentions(id);
	}
	
}
