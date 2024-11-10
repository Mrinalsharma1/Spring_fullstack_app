package com.happiest.apigateway.service;

import com.happiest.apigateway.model.AuthResponse;
import com.happiest.apigateway.model.Users;
import com.happiest.apigateway.repository.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public Users register(Users user){
        return repo.save(user);

    }

    public AuthResponse verify(Users user) {
        LOGGER.info("Authenticating user with Username: " + user.getUsername());
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                LOGGER.info("User logged in successfully");
                String token = jwtService.generateToken(user.getUsername());

                // Fetch the complete user object after authentication
                Users authenticatedUser = repo.findByUsername(user.getUsername());

                // Return the complete user data in AuthResponse

                return new AuthResponse(
                        token,
                        authenticatedUser.getId(),
                        authenticatedUser.getUsername(),
                        authenticatedUser.getProfilename(),
                        authenticatedUser.getRole()
                );

            }


        } catch (BadCredentialsException e) {
            LOGGER.error("Invalid username or password for user: " + user.getUsername(), e);
            return null; // Return null if authentication fails due to invalid credentials
        }
        catch (NullPointerException e) {
            // Handle null values explicitly if needed
            LOGGER.error("Null value encountered during authentication", e);
            throw new RuntimeException("Null value in user details"); // Rethrow or handle as appropriate
        } catch (Exception e) {
            LOGGER.error("An error occurred during authentication", e);
            // Optionally, you can rethrow or handle other exceptions differently
            throw e; // Rethrow or handle as needed
        }
        return null; // In case of any unexpected situation
    }
}

