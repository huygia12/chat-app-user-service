package org.service.user.chat_app_user_service.repository;

import jakarta.transaction.Transactional;
import org.service.user.chat_app_user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, BigInteger> {

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);

	@Transactional
	void deleteByEmail(String email);

}
