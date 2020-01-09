package inc.sebec.carcare.core.security.component;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import inc.sebec.carcare.core.security.model.Role;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {
	private static final String AUTHORITIES_KEY = "auth";

	@Value("${token.secret.key:secret}")
	private String secretKey;

	@Value("${token.expiration.time:200000}")
	private Long expirationTime; //in Milli Seconds

	public String createToken(Authentication authentication) {
		var authorities = authentication.getAuthorities().stream()
										.map(GrantedAuthority::getAuthority)
										.collect(Collectors.joining(","));

		var expiration = new Date(System.currentTimeMillis() + expirationTime);

		return Jwts.builder()
				   .setSubject(authentication.getName())
				   .claim(AUTHORITIES_KEY, authorities)
				   .signWith(SignatureAlgorithm.HS512, secretKey)
				   .setExpiration(expiration)
				   .compact();
	}

	public Authentication getAuthentication(String token) {
		if (StringUtils.isEmpty(token) || !validateToken(token)) {
			throw new BadCredentialsException("Invalid token");
		}
		var claims = Jwts.parser()
						 .setSigningKey(secretKey)
						 .parseClaimsJws(token)
						 .getBody();

		var authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
								.map(Role::new)
								.collect(Collectors.toList());

		var principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			log.info("Invalid JWT signature.");
			log.trace("Invalid JWT signature trace: ", e);
		} catch (MalformedJwtException e) {
			log.info("Invalid JWT token.");
			log.trace("Invalid JWT token trace: ", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
			log.trace("Expired JWT token trace: ", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token.");
			log.trace("Unsupported JWT token trace: ", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT token compact of handler are invalid.");
			log.trace("JWT token compact of handler are invalid trace: ", e);
		}
		return false;
	}
}