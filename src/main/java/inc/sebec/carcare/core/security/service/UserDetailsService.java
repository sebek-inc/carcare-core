package inc.sebec.carcare.core.security.service;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import inc.sebec.carcare.core.security.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements ReactiveUserDetailsService {
	private final UserDetailsRepository repository;

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return repository.findByUsername(username)
						 .map(user -> new User(user.getUsername(), user.getPassword(), user.getAuthorities()));
	}


}
