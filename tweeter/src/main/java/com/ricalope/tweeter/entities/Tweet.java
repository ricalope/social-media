package com.ricalope.tweeter.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Tweet {

	@Id
	@GeneratedValue
	private Long id;
	
	private String content;
	
	@ManyToOne
	@JoinColumn
	private User author;
	
	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp posted;
	
	@Column(nullable = false)
	private boolean deleted = false;
	
	@ManyToOne
	private Tweet inReplyTo;
	
	@OneToMany(mappedBy = "inReplyTo")
	private List<Tweet> replies = new ArrayList<>();
	
	@ManyToOne
	private Tweet repostOf;
	
	@OneToMany(mappedBy = "repostOf")
	private List<Tweet> reposts = new ArrayList<>();
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "tweet_hashtags",
		joinColumns = @JoinColumn(name = "tweet_id"),
		inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<Hashtag> hashtags = new ArrayList<>();
	
	@ManyToMany(mappedBy = "likedTweets")
	Set<User> likes = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = "user_mentions",
		joinColumns = @JoinColumn(name = "tweet_id"),
		inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> mentions = new ArrayList<>();
	
	@Override
	public int hashCode() {
		return id.intValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Tweet) {
			return ((Tweet) obj).getId().equals(this.getId());
		}
		return false;
	}
	
	public void addMention(User user) {
		mentions.add(user);
	}
	
	
}
