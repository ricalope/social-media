package com.ricalope.tweeter.services;

public interface ValidateService {

	boolean checkTagExists(String label);

	boolean checkUsernameExists(String username);

	boolean checkUsernameAvailable(String username);

}
