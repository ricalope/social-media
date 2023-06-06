package com.ricalope.tweeter.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Hashtag {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String label;
	
	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp firstUsed;

	@UpdateTimestamp
	@Column(nullable = false)
	private Timestamp lastUsed;

	@ManyToMany(mappedBy="hashtags", cascade=CascadeType.MERGE)
	private List<Tweet> hashtaggedTweets;
	
}
