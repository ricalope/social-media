package com.ricalope.tweeter.model;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TweetResponseDto {
	
	private Long id;
	
	private UserResponseDto author;
	
	private Timestamp posted;
	
	private String content;
	
	private TweetResponseDto inReplyTo;
	
	private TweetResponseDto repostOf;

}
