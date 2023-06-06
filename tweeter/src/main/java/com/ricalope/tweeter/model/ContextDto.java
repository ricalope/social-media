package com.ricalope.tweeter.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContextDto {
	
	private TweetResponseDto target;
	
	private List<TweetResponseDto> before = new ArrayList<>();
	
	private List<TweetResponseDto> after = new ArrayList<>();

}
