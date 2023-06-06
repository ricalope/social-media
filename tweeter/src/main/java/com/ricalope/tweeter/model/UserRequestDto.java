package com.ricalope.tweeter.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequestDto {
	
	private CredentialsDto credentials;
	
	private ProfileDto profile;

}
