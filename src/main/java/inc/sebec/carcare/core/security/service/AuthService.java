package inc.sebec.carcare.core.security.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;

import inc.sebec.carcare.core.dto.request.LoginRequest;
import inc.sebec.carcare.core.dto.response.LoginResponse;
import inc.sebec.carcare.core.security.component.JWTAuthManager;
import inc.sebec.carcare.core.security.component.TokenProvider;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final TokenProvider tokenProvider;
	private final JWTAuthManager authenticationManager;

	public Mono<LoginResponse> authorize(LoginRequest user) {
		var authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		var authentication = authenticationManager.authenticate(authenticationToken);
		authentication.doOnError(throwable -> {throw new BadCredentialsException("Bad credentials");});
		ReactiveSecurityContextHolder.withAuthentication(authenticationToken);
		return authentication.map(auth -> new LoginResponse(tokenProvider.createToken(auth)));
	}
}
