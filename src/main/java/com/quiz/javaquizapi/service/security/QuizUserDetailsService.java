package com.quiz.javaquizapi.service.security;

import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.model.user.QuizUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public record QuizUserDetailsService(UserRepository userRepository) implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new QuizUserDetails(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user by username '%s'"
                        .formatted(username))));
    }
}
