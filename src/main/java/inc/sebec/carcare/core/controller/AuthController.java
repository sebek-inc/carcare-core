package inc.sebec.carcare.core.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import inc.sebec.carcare.core.dto.request.LoginRequest;
import inc.sebec.carcare.core.dto.response.LoginResponse;
import inc.sebec.carcare.core.security.model.Role;
import inc.sebec.carcare.core.security.model.UserDetail;
import inc.sebec.carcare.core.security.repository.UserDetailsRepository;
import inc.sebec.carcare.core.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(API.V1.AUTH)
@RequiredArgsConstructor
public class AuthController {
	private final UserDetailsRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final AuthService authService;


	@PostMapping("signup/user")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<UserDetail> createUser(@RequestBody UserDetail user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setAuthorities(Set.of(new Role("ROLE_USER")));
		return repository.save(user);
	}

	@PostMapping("signup/admin")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<UserDetail> createAdmin(@RequestBody UserDetail user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setAuthorities(Set.of(new Role("ROLE_ADMIN")));
		return repository.save(user);
	}

	@PostMapping(value = "signin")
	public Mono<LoginResponse> authorize(@RequestBody LoginRequest user) {
		return authService.authorize(user);
	}

}