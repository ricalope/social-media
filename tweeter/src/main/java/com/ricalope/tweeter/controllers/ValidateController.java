package com.ricalope.tweeter.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ricalope.tweeter.services.ValidateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class ValidateController {
	
	private final ValidateService validateService;
	
	@GetMapping("/tag/exists/{label}")
	public boolean checkTagExists(@PathVariable String label) {
		return validateService.checkTagExists(label);
	}
	
	@GetMapping("/username/exists/@{username}")
	public boolean checkUsernameExists(@PathVariable String username) {
		return validateService.checkUsernameExists(username);
	}
	
	@GetMapping("/username/available/@{username}")
	public boolean checkUsernameAvailable(@PathVariable String username) {
		return validateService.checkUsernameAvailable(username);
	}

}
