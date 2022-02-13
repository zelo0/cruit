package com.project.cruit.authentication;

import com.project.cruit.domain.User;
import com.project.cruit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException("해당 이메일로 가입된 유저는 없습니다");
        }

        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("해당 이메일로 가입된 유저는 없습니다");
        }

        return new SessionUser(user);
    }
}
