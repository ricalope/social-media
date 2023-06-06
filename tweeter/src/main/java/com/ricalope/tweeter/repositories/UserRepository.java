package com.ricalope.tweeter.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ricalope.tweeter.entities.User;
import com.ricalope.tweeter.model.CredentialsDto;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByCredentialsUsername(String username);

	Optional<User> findByCredentials(CredentialsDto credentials);

	Optional<User> findTopByCredentialsUsername(String username);

}
