package com.firesale.api.service;


import com.firesale.api.repository.UserRepository;
import com.firesale.api.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return new UserPrincipal(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user with email: %s was found", email))));
    }

}
