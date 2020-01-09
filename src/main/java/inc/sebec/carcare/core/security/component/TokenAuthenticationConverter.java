package inc.sebec.carcare.core.security.component;

import java.util.Objects;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationConverter implements ServerAuthenticationConverter {
	private static final String BEARER = "Bearer ";
	private final TokenProvider tokenProvider;

	@Override
	public Mono<Authentication> convert(ServerWebExchange exchange) {
		return Mono.justOrEmpty(exchange)
				   .map(this::getTokenFromRequest)
				   .filter(authValue -> Objects.nonNull(authValue) && authValue.length() > BEARER.length())
				   .map(authValue -> authValue.substring(BEARER.length()))
				   .filter(token -> !StringUtils.isEmpty(token))
				   .map(tokenProvider::getAuthentication)
				   .filter(Objects::nonNull);
	}

	private String getTokenFromRequest(ServerWebExchange serverWebExchange) {
		String token = serverWebExchange.getRequest()
										.getHeaders()
										.getFirst(HttpHeaders.AUTHORIZATION);
		return StringUtils.isEmpty(token) ? Strings.EMPTY : token;
	}
}
