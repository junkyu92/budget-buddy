package junkyu.budget.service;

import junkyu.budget.config.provider.JwtTokenProvider;
import junkyu.budget.domain.User;
import junkyu.budget.dto.SigninRequestDto;
import junkyu.budget.dto.SigninResponseDto;
import junkyu.budget.dto.SignupDto;
import junkyu.budget.dto.TokenIssuanceDto;
import junkyu.budget.enums.UserRole;
import junkyu.budget.exception.CustomException;
import junkyu.budget.exception.ErrorCode;
import junkyu.budget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupDto signupDto){
        Optional<User> findUser = userRepository.findByAccount(signupDto.getAccount());

        if(findUser.isPresent()) throw new CustomException(ErrorCode.DUPLICATED_ACCOUNT);

        userRepository.save(User.builder()
                        .account(signupDto.getAccount())
                        .pw(passwordEncoder.encode(signupDto.getPw()))
                        .userRole(UserRole.ROLE_USER)
                        .build());
    }

    public SigninResponseDto signin(SigninRequestDto signinRequestDto){
        User findUser = userRepository.findByAccount(signinRequestDto.getAccount())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        passwordMatch(signinRequestDto.getPw(), findUser.getPw());

        String accessToken = jwtTokenProvider.generateAccessToken(TokenIssuanceDto.from(findUser));
        String refreshToken = jwtTokenProvider.generateRefreshToken(findUser.getAccount());

        return new SigninResponseDto(accessToken, refreshToken);
    }

    public void passwordMatch(String pw1, String pw2){
        if(!passwordEncoder.matches(pw1, pw2)) throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
    }

    public List<User> findAllByNotification(){
        return userRepository.findAllByNotification(true);
    }
}
