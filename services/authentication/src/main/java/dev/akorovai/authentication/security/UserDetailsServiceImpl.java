package dev.akorovai.authentication.security;

import dev.akorovai.authentication.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository repository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("Loading user by username: {}", username);
		try {
			UserDetails userDetails = repository.findByNickname(username)
					                          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
			log.info("User found: {}", username);
			return userDetails;
		} catch (UsernameNotFoundException e) {
			log.error("User not found: {}", username);
			throw e;
		} catch (Exception e) {
			log.error("Error loading user by username: {}", username, e);
			throw e;
		}
	}
}