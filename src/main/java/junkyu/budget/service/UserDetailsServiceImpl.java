package junkyu.budget.service;

import junkyu.budget.domain.User;
import junkyu.budget.exception.CustomException;
import junkyu.budget.exception.ErrorCode;
import junkyu.budget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<GrantedAuthority> authorities = user.getUserRole().getAuthorities();

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getAccount())
                .password(user.getPw())
                .authorities(authorities)
                .build();
    }
}
