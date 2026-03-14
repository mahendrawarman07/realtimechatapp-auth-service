package com.example.authservice.services;


import com.example.authservice.exceptions.ExpiredTokenException;
import com.example.authservice.exceptions.InvalidTokenException;
import com.example.authservice.exceptions.PasswordMismatchException;
import com.example.authservice.exceptions.UnknownUserException;
import com.example.authservice.models.User;
import com.example.authservice.repositories.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private BCryptPasswordEncoder bcryptPasswordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

//    private SecretKey getSigningKey() {
//        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secretKey); // ✅ Base64URL
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bcryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }

    @Override
    public User signUp(String name, String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bcryptPasswordEncoder.encode(password));
        user = userRepository.save(user);
        return user;
    }

    @Override
    public String login(String email, String password) throws UnknownUserException,PasswordMismatchException{
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()) {
            // User not found, have to register first
            throw new UnknownUserException("User with email " + email + " not found. Please register first.");
        }

        User user = optionalUser.get();

        if(!bcryptPasswordEncoder.matches(password, user.getPassword())) {
            // Invalid credentials
            throw new PasswordMismatchException("Incorrect password.");
        }

        Map<String, Object> claims = Map.of("real time chat application", "www.realtimechatapp.com",
                                 "userId", user.getId(), "email", user.getEmail());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 10); // Token valid for 2 minutes

        Date expirationDate = calendar.getTime();

//        String jwtToken = JwtUtil.generateToken(claims, user.getEmail(), expirationDate);

//        String jwtToken = Jwts.builder()
//                .setClaims(claims)
//                .setSubject(user.getEmail())
//                .setIssuedAt(new Date())
//                .setExpiration(expirationDate)
//                .signWith(JwtUtil.getSecretKey()) // Use the secret key from JwtUtil
//                .compact();

//        SecretKey key = Jwts.SIG.HS256.key().build();
//        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
//        System.out.println(base64Key); // copy this value and set it as the SECRET in JwtUtil

        SecretKey key = Jwts.SIG.HS256.key().build();
        // Change this ↓
        String base64Key = Base64.getUrlEncoder().encodeToString(key.getEncoded()); // ✅ Base64URL
        System.out.println(base64Key);

        String jwtToken = Jwts.builder().claims(claims).signWith(getSigningKey()).expiration(expirationDate).compact();

        return jwtToken;
    }

    public User validateToken(String tokenValue) throws InvalidTokenException, ExpiredTokenException {
        try {
            JwtParser jwtParser = Jwts.parser().verifyWith(getSigningKey()).build();
            Claims claims = jwtParser.parseSignedClaims(tokenValue).getPayload();

            UUID userId = UUID.fromString((String) claims.get("userId"));
            Optional<User> optionalUser = userRepository.findById(userId);
            return optionalUser.get();

        } catch (ExpiredJwtException e) {
            System.out.println("Token is expired.");
            throw new ExpiredTokenException("Token has expired.");
        } catch (JwtException e) {
            System.out.println("Invalid token: " + e.getMessage());
            throw new InvalidTokenException("Invalid JWT token.");
        }
    }

//     public User validateToken(String tokenValue){
//
//         JwtParser jwtParser = Jwts.parser().verifyWith(getSigningKey()).build();
//         Claims claims = jwtParser.parseSignedClaims(tokenValue).getPayload();
//
//         System.out.println("claims "+claims);
//
//         //        Date expiryDate = (Date) claims.get("exp");
////        Date currentDate = new Date();
//
//         Long expiryTime = (Long) claims.get("exp");
//         Long currentTime = System.currentTimeMillis();
//         // here we are comparing expiry time and current time in milliseconds, but the expiry time is in seconds, so we need to convert it to milliseconds before comparing.
//         expiryTime = expiryTime * 1000; // this will convert the expiry time from seconds to milliseconds.
//
//         if (expiryTime < currentTime) {
//             //Token is InValid.
//
//             //TODO - Check expiry Time and current time (Milliseconds vs Seconds) issue. // done
//             System.out.println("Expiry time : " + expiryTime);
//             System.out.println("Current time : " + currentTime);
//
//             System.out.println("Token is expired.");
//             return null;
////             throw new InvalidTokenException("Invalid JWT token.");
//         }
//         //Token is Valid.
//         UUID userId = UUID.fromString((String) claims.get("userId"));
//         Optional<User> optionalUser = userRepository.findById(userId);
//
//         System.out.println(userId+" "+optionalUser);
//         return optionalUser.get();
//     }

    public User getUserByEmail(String email) throws UnknownUserException {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()) {
            throw new UnknownUserException("User with email " + email + " not found.");
        }

        return optionalUser.get();
    }
}
