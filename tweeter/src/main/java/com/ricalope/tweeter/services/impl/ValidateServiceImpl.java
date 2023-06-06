package com.ricalope.tweeter.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ricalope.tweeter.entities.Hashtag;
import com.ricalope.tweeter.entities.User;
import com.ricalope.tweeter.repositories.HashtagRepository;
import com.ricalope.tweeter.repositories.UserRepository;
import com.ricalope.tweeter.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	
	private final HashtagRepository hashtagRepository;
	
	private final UserRepository userRepository;
	
	@Override
	public boolean checkTagExists(String label) {
		Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);
		return optionalHashtag.isPresent();
	}
	
	@Override
	public boolean checkUsernameExists(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		return optionalUser.isPresent();
	}
	
	@Override
	public boolean checkUsernameAvailable(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		return optionalUser.isEmpty();
	}

}
