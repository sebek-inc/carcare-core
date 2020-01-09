package inc.sebec.carcare.core.security.component;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class HeadersExchangeMatcher implements ServerWebExchangeMatcher {
	@Override
	public Mono<MatchResult> matches(ServerWebExchange exchange) {
		Mono<ServerHttpRequest> request = Mono.just(exchange).map(ServerWebExchange::getRequest);
		return request.map(ServerHttpRequest::getHeaders)
					  .filter(h -> h.containsKey(HttpHeaders.AUTHORIZATION))
					  .flatMap($ -> MatchResult.match())
					  .switchIfEmpty(MatchResult.notMatch());
	}
}
