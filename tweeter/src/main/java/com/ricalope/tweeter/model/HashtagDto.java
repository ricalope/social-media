package com.ricalope.tweeter.model;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HashtagDto {
	
	private String label;
	
	private Timestamp firstUsed;
	
	private Timestamp lastUsed;

}
