package com.ricalope.tweeter.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ricalope.tweeter.entities.Hashtag;
import com.ricalope.tweeter.entities.Tweet;
import com.ricalope.tweeter.entities.User;
import com.ricalope.tweeter.exceptions.BadRequestException;
import com.ricalope.tweeter.exceptions.NotAuthorizedException;
import com.ricalope.tweeter.exceptions.NotFoundException;
import com.ricalope.tweeter.mappers.HashtagMapper;
import com.ricalope.tweeter.mappers.TweetMapper;
import com.ricalope.tweeter.mappers.UserMapper;
import com.ricalope.tweeter.model.ContextDto;
import com.ricalope.tweeter.model.CredentialsDto;
import com.ricalope.tweeter.model.HashtagDto;
import com.ricalope.tweeter.model.TweetRequestDto;
import com.ricalope.tweeter.model.TweetResponseDto;
import com.ricalope.tweeter.model.UserResponseDto;
import com.ricalope.tweeter.repositories.HashtagRepository;
import com.ricalope.tweeter.repositories.TweetRepository;
import com.ricalope.tweeter.repositories.UserRepository;
import com.ricalope.tweeter.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	private final UserRepository userRepository;
	private final HashtagRepository hashtagRepository;
	
	private final TweetMapper tweetMapper;
	private final HashtagMapper hashtagMapper;
	private final UserMapper userMapper;
	
	private User getUser(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		if(optionalUser.isEmpty()) {
			throw new NotFoundException("The specified user could not be found.");
		} else {
			return optionalUser.get();			
		}
	}
	
	private void validateUserCredentials(User userOne, User userTwo) {
		if(!userOne.getCredentials().getUsername().equals(userTwo.getCredentials().getUsername()) ||
			!userOne.getCredentials().getPassword().equals(userTwo.getCredentials().getPassword())) {
			throw new NotAuthorizedException("The supplied credentials do not match those of the specified user.");
		}
	}
	
	private void checkUserDeleted(User user) {
		if(user.isDeleted()) {
			throw new BadRequestException("The specified user has been deleted.");
		}
	}
	
	private Tweet getTweet(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findById(id);
		if(optionalTweet.isEmpty()) {
			throw new NotFoundException("The specified tweet could not be found.");
		}
		return optionalTweet.get();
	}
	
	private void checkTweetDeleted(Tweet tweet) {
		if(tweet.isDeleted()) {
			throw new BadRequestException("The specified tweet has been deleted.");
		}
	}
	
	private List<User> parseMentions(String content) {
		List<User> userMentions = new ArrayList<>();
		Map<String, User> dbUsernames = new HashMap<>();
		
		for(User user : userRepository.findAll()) {
			dbUsernames.put(user.getCredentials().getUsername(), user);
		}
		
		String[] wordsInContent = content.split("\\s+");
		for(String word : wordsInContent) {
			if(word.startsWith("@")) {
				if(dbUsernames.get(word.substring(1)) != null) {
					userMentions.add(dbUsernames.get(word.substring(1)));
				}
			}
		}
		return userMentions;
	}
	
	private List<Hashtag> parseHashtags(String content) {
		List<Hashtag> hashtags = new ArrayList<>();
		Map<String, Hashtag> dbHashtags = new HashMap<>();
		
		for(Hashtag hashtag : hashtagRepository.findAll()) {
			dbHashtags.put(hashtag.getLabel(), hashtag);
		}
		
		String[] wordsInContent = content.split("\\s+");
		for(String word : wordsInContent) {
			if(word.startsWith("#")) {
				if(dbHashtags.get(word.substring(1)) != null) {
					hashtags.add(dbHashtags.get(word.substring(1)));
				} else {
					Hashtag newHashtag = new Hashtag();
					newHashtag.setLabel(word.substring(1));
					hashtagRepository.saveAndFlush(newHashtag);
					hashtags.add(newHashtag);
				}
			}
		}
		return hashtags;
	}
	
	@Override
	public List<TweetResponseDto> getAllTweets() {
		List<Tweet> allTweets = tweetRepository.findAll()
				.stream().filter(tweet -> !tweet.isDeleted())
				.collect(Collectors.toList());
		return tweetMapper.entitiesToDtos(allTweets);
	}
	
	@Override
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
		if (tweetRequestDto.getContent() == null || tweetRequestDto.getCredentials() == null
				|| tweetRequestDto.getCredentials().getPassword() == null) {
			throw new BadRequestException("The content and credentials are required fields");
		}
		User loggedInUser = getUser(tweetRequestDto.getCredentials().getUsername());
		checkUserDeleted(loggedInUser);
		
		Tweet tweetToSave = tweetMapper.dtoToEntity(tweetRequestDto);
//		tweetRepository.save(tweetToSave);
		tweetToSave.setHashtags(parseHashtags(tweetToSave.getContent()));
		tweetToSave.setMentions(parseMentions(tweetToSave.getContent()));
//		tweetToSave.setContent(tweetRequestDto.getContent());
		tweetToSave.setAuthor(loggedInUser);
		loggedInUser.getTweets().add(tweetToSave);
		tweetRepository.saveAndFlush(tweetToSave);
		return tweetMapper.entityToDto(tweetToSave);
	}
	
	@Override
	public TweetResponseDto getTweetById(Long id) {
		Tweet queriedTweet = getTweet(id);
		checkTweetDeleted(queriedTweet);
		return tweetMapper.entityToDto(queriedTweet);
	}
	
	@Override
	public TweetResponseDto deleteTweet(Long id, CredentialsDto credentialsDto) {
		Tweet tweetToDelete = getTweet(id);
		checkTweetDeleted(tweetToDelete);
		User tweetAuthor = tweetToDelete.getAuthor();
		User loggedInUser = getUser(credentialsDto.getUsername());
		validateUserCredentials(tweetAuthor, loggedInUser);
		tweetToDelete.setDeleted(true);
		tweetRepository.saveAndFlush(tweetToDelete);
		return tweetMapper.entityToDto(tweetToDelete);
	}
	
	@Override
	public void addLikeToTweet(Long id, CredentialsDto credentialsDto) {
		Tweet tweetToLike = getTweet(id);
		checkTweetDeleted(tweetToLike);
		User loggedInUser = getUser(credentialsDto.getUsername());
		checkUserDeleted(loggedInUser);
		userRepository.saveAndFlush(loggedInUser);		
		tweetToLike.getLikes().add(loggedInUser);
		loggedInUser.getLikedTweets().add(tweetToLike);
		tweetRepository.saveAndFlush(tweetToLike);
	}
	
	@Override
	public TweetResponseDto createReply(Long id, TweetRequestDto tweetRequestDto) {
		if (tweetRequestDto.getContent() == null || tweetRequestDto.getCredentials() == null
				|| tweetRequestDto.getCredentials().getPassword() == null) {
			throw new BadRequestException("The content and credentials are required fields");
		}
		
		User tweetAuthor = getUser(tweetRequestDto.getCredentials().getUsername());
		Tweet replyTweet = tweetMapper.dtoToEntity(tweetRequestDto);
		Tweet tweetToReplyTo = getTweet(id);
		
		tweetToReplyTo.getReplies().add(replyTweet);
		replyTweet.setAuthor(tweetAuthor);
		replyTweet.setContent(replyTweet.getContent());
		replyTweet.setHashtags(parseHashtags(replyTweet.getContent()));
		replyTweet.setMentions(parseMentions(replyTweet.getContent()));
		replyTweet.setInReplyTo(tweetToReplyTo);
		tweetRepository.saveAndFlush(replyTweet);
		return tweetMapper.entityToDto(replyTweet);
	}
	
	@Override
	public TweetResponseDto createRepost(Long id, CredentialsDto credentialsDto) {
		Tweet tweetToRepost = getTweet(id);
		checkTweetDeleted(tweetToRepost);
		User repostAuthor = getUser(credentialsDto.getUsername());
		
		Tweet repost = new Tweet();
		
		repost.setRepostOf(tweetToRepost);
		repost.setAuthor(repostAuthor);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(repost));
	}
	
	@Override
	public List<HashtagDto> getTweetTags(Long id) {
		Tweet queriedTweet = getTweet(id);
		checkTweetDeleted(queriedTweet);
		return hashtagMapper.entitiesToDtos(queriedTweet.getHashtags());
	}
	
	@Override
	public List<UserResponseDto> getAllTweetLikes(Long id) {
		Tweet queriedTweet = getTweet(id);
		checkTweetDeleted(queriedTweet);
		List<User> tweetLikes = queriedTweet.getLikes()
				.stream().filter(user -> !user.isDeleted())
				.collect(Collectors.toList());
		return userMapper.entitiesToDtos(tweetLikes);
	}
	
	@Override
	public ContextDto getTweetContext(Long id) {
		Tweet queriedTweet = getTweet(id);
		checkTweetDeleted(queriedTweet);
		
		Tweet beforeTweet = queriedTweet.getInReplyTo();
		List<Tweet> before = new ArrayList<>();
		List<Tweet> after = queriedTweet.getReplies()
				.stream().filter(tweet -> !tweet.isDeleted())
				.collect(Collectors.toList());
		List<Tweet> afterTweets = new ArrayList<>();
		
		afterTweets.addAll(after);
		
		while(beforeTweet != null) {
			before.add(beforeTweet);
			beforeTweet = beforeTweet.getInReplyTo();
		}
		
		for(Tweet tweet : after) {
			if(tweet.getReplies() != null) {
				afterTweets.addAll(tweet.getReplies());
			}
		}
		ContextDto context = new ContextDto();
		context.setBefore(tweetMapper.entitiesToDtos(before));
		context.setTarget(tweetMapper.entityToDto(queriedTweet));
		context.setAfter(tweetMapper.entitiesToDtos(afterTweets));
		return context;
	}
	
	@Override
	public List<TweetResponseDto> getTweetReplies(Long id) {
		Tweet queriedTweet = getTweet(id);
		List<Tweet> tweetReplies = queriedTweet.getReplies()
				.stream().filter(tweet -> !tweet.isDeleted())
				.collect(Collectors.toList());
		return tweetMapper.entitiesToDtos(tweetReplies);
	}
	
	@Override
	public List<TweetResponseDto> getTweetReposts(Long id) {
		Tweet queriedTweet = getTweet(id);
		List<Tweet> tweetReposts = queriedTweet.getReposts()
				.stream().filter(tweet -> !tweet.isDeleted())
				.collect(Collectors.toList());
		return tweetMapper.entitiesToDtos(tweetReposts);
	}
	
	@Override
	public List<UserResponseDto> getTweetMentions(Long id) {
		Tweet queriedTweet = getTweet(id);
		List<User> mentions = queriedTweet.getMentions()
				.stream().filter(user -> !user.isDeleted())
				.collect(Collectors.toList());
		return userMapper.entitiesToDtos(mentions);
	}

}
