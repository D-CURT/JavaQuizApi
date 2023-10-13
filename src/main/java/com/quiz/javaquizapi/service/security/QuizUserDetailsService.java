package com.quiz.javaquizapi.service.security;

import com.quiz.javaquizapi.model.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.model.user.QuizUserDetails;
import org.springframework.stereotype.Service;

/**
 * Provides functionality to operate with security user details.
 */
@Service
public record QuizUserDetailsService(UserRepository userRepository) implements UserDetailsService {
    /**
     * Creates user details based on an existing user.
     *
     * @param username username field value.
     *
     * @return created {@link UserDetails}.
     *
     * @throws UsernameNotFoundException if a user wasn't found by an accepted username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new QuizUserDetails(userRepository.findByUsername(username)
                .filter(User::isEnabled)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user by username '%s'"
                        .formatted(username))));
    }
}
