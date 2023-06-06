package com.ricalope.tweeter.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ricalope.tweeter.model.HashtagDto;
import com.ricalope.tweeter.model.TweetResponseDto;
import com.ricalope.tweeter.services.HashtagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class HashtagController {
	
	private final HashtagService hashtagService;
	
	@GetMapping
	public List<HashtagDto> getAllHashtags() {
		return hashtagService.getAllHashtags();
	}
	
	@GetMapping("/{label}")
	public List<TweetResponseDto> getTaggedTweets(@PathVariable String label) {
		return hashtagService.getTaggedTweets(label);
	}

}
