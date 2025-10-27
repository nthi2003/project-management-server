package com.skytech.projectmanagement.user.service;

import java.util.ArrayList;
import java.util.List;
import com.skytech.projectmanagement.common.exception.ResourceNotFoundException;
import com.skytech.projectmanagement.user.entity.User;
import com.skytech.projectmanagement.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =
                userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy người dùng với email: " + email));

        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (Boolean.TRUE.equals(user.getIsProductOwner())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_PRODUCT_OWNER"));
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getHashPassword(), authorities);
    }

}
