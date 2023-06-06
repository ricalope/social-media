package com.ricalope.tweeter.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TweetRequestDto {
	
	private String content;
	
	private CredentialsDto credentials;

}
