package inc.sebec.carcare.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

import inc.sebec.carcare.core.security.component.HeadersExchangeMatcher;
import inc.sebec.carcare.core.security.component.JWTAuthManager;
import inc.sebec.carcare.core.security.component.TokenAuthenticationConverter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	private final JWTAuthManager jwtAuthManager;
	private final TokenAuthenticationConverter tokenAuthenticationConverter;
	private final HeadersExchangeMatcher headersExchangeMatcher;

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http.httpBasic().disable()
			.formLogin().disable()
			.csrf().disable()
			.logout().disable()
			.authorizeExchange()
			.pathMatchers("/api/v1/auth/signin")
			.permitAll()
			.and()
			.authorizeExchange()
			.pathMatchers("/api/v1/auth/signup/**")
			.hasRole("ADMIN")
			.and()
			.authorizeExchange()
			.anyExchange()
			.authenticated()
			.and()
			.addFilterAt(webFilter(), SecurityWebFiltersOrder.AUTHORIZATION);

		return http.build();

	}

	public AuthenticationWebFilter webFilter() {
		var authenticationWebFilter = new AuthenticationWebFilter(jwtAuthManager);
		authenticationWebFilter.setServerAuthenticationConverter(tokenAuthenticationConverter);
		authenticationWebFilter.setRequiresAuthenticationMatcher(headersExchangeMatcher);
		authenticationWebFilter.setSecurityContextRepository(new WebSessionServerSecurityContextRepository());
		return authenticationWebFilter;
	}


}
