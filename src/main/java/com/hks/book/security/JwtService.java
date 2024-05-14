package com.hks.book.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	@Value("${application.security.jwt.expiration}")
	private long jwtExpiration;
	@Value("${application.security.jwt.secret_key}")
	private String secretKey;
	
	
	public String extractUsername(String token) {
		return extractClaims(token,Claims::getSubject);
	}

	private <T> T extractClaims(String token, Function<Claims, T>claimsResolver ) {
		final Claims claims=extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	@SuppressWarnings("deprecation")
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				;
	}
	
//	public String generateToken(UserDetails userDetails) {
//		return generateToken(new HashMap<String,Object>(),userDetails);
//	}

	public String generateToken(HashMap<String, Object> extraCliams, UserDetails userDetails) {
		return buildToken(extraCliams,userDetails,jwtExpiration);
	}

 	@SuppressWarnings("deprecation")
	private String buildToken(
			HashMap<String, Object> extraCliams,
			UserDetails userDetails,
			long jwtExpiration) {
		
		  var authorities=userDetails.getAuthorities()
				  .stream()
				  .map(GrantedAuthority::getAuthority)
				  .toList();
	
		return	Jwts.builder()
			.setClaims(extraCliams)
			.setSubject(userDetails.getUsername())
			.setIssuedAt( new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis()+jwtExpiration))
			.claim("authorities", authorities)
			.signWith(getSignKey())
			.compact();
	}
 	
 	public boolean isValidToken(String token,UserDetails userDetails) {
 		final String username=extractUsername(token);
 		return (username.equals(userDetails.getUsername())&&isTokenExpired(token));
 	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaims(token, Claims::getExpiration);
	}

	

	private Key getSignKey() {
		 byte []keyBytes=Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
