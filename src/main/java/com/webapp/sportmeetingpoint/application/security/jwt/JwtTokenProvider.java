package com.webapp.sportmeetingpoint.application.security.jwt;


import com.webapp.sportmeetingpoint.domain.entities.UserRole;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

  @Value("${jwt.token.secret}")
  private String secret;

  @Value("${jwt.token.expired}")
  private Long validityInMilliseconds;

  @Autowired
  private UserDetailsService userDetailsService;

  @PostConstruct
  protected void init(){
    secret = Base64.getEncoder().encodeToString(secret.getBytes());
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  public String createToken(String userName, List<UserRole> userRoles){

    Claims claims = Jwts.claims().setSubject(userName);
    claims.put("roles", getRolenames(userRoles));

    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
  }

  public UsernamePasswordAuthenticationToken getAuthentication(String token) {
    JwtUser userDetails = (JwtUser)this.userDetailsService.loadUserByUsername(getUsername(token));
    userDetails.setToken(token);

    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
      return bearerToken.substring(7, bearerToken.length());
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

      if (claims.getBody().getExpiration().before(new Date())) {
        return false;
      }

      return true;
    } catch (JwtException | IllegalArgumentException e) {
      throw new JwtAuthenticationException("JWT token is expired or invalid");
    }
    
  }

//  public Date jwtTokenGetExpirationDate(String token){
//    Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
//    return claims.getBody().getExpiration();
//  }

  private List<String> getRolenames(List<UserRole> userRoles){
    List<String> result = new ArrayList<>();

    userRoles.forEach( role -> result.add(role.getName()));
    return result;
  }


}
