package com.hks.book.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(
		 @NotNull	HttpServletRequest request, 
		 @NotNull	HttpServletResponse response, 
		 @NotNull   FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
       if(request.getServletPath().contains("/api/v1/auth")) {
    	   filterChain.doFilter(request, response);
    	   return;
       }
       
       String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
       
       if(authHeader==null||!authHeader.startsWith("Bearer ")) {
    	   filterChain.doFilter(request, response);
    	   return;
       }
       
       final String token=authHeader.substring(7);
       final String userEmail=jwtService.extractUsername(token);
       if(userEmail!=null&&SecurityContextHolder.getContext().getAuthentication()==null) {
    	   UserDetails userDetails=userDetailsService.loadUserByUsername(userEmail);
    	   UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
    			   userDetails,
    			   null,
    			   userDetails.getAuthorities()
    			   );
    	     authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    	     SecurityContextHolder.getContext().setAuthentication(authToken);
       }
		filterChain.doFilter(request, response);
	}

}