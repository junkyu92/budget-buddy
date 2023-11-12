package junkyu.budget.service;

import junkyu.budget.config.provider.JwtTokenProvider;
import junkyu.budget.domain.User;
import junkyu.budget.dto.SigninRequestDto;
import junkyu.budget.dto.SigninResponseDto;
import junkyu.budget.dto.SignupDto;
import junkyu.budget.exception.CustomException;
import junkyu.budget.exception.ErrorCode;
import junkyu.budget.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks private UserService userService;

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    private final String account = String.valueOf(UUID.randomUUID());
    private final String pw = String.valueOf(UUID.randomUUID());
    @Test
    void 회원가입(){
        SignupDto signupDto = SignupDto.builder().account(account).build();
        userService.signup(signupDto);

        verify(userRepository).save(any(User.class));
    }
    @Test
    void 회원가입_실패_이미_가입된_계정(){
        SignupDto signupDto = SignupDto.builder().account(account).build();
        when(userRepository.findByAccount(account)).thenReturn(Optional.ofNullable(User.builder().build()));

        assertThatThrownBy(() -> userService.signup(signupDto))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DUPLICATED_ACCOUNT.getMessage());
    }

    @Test
    void 로그인(){
        SigninRequestDto signinRequestDto = SigninRequestDto.builder().account(account).pw(pw).build();
        when(userRepository.findByAccount(account)).thenReturn(Optional.ofNullable(User.builder().pw(pw).build()));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        assertThat(userService.signin(signinRequestDto)).isInstanceOf(SigninResponseDto.class);
    }

    @Test
    void 로그인_실패_존재하지_않는_유저(){
        SigninRequestDto signinRequestDto = SigninRequestDto.builder().account(account).pw(pw).build();
        when(userRepository.findByAccount(account)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.signin(signinRequestDto))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    void 로그인_실패_비밀번호_불일치(){
        SigninRequestDto signinRequestDto = SigninRequestDto.builder().account(account).pw(pw).build();
        when(userRepository.findByAccount(account)).thenReturn(Optional.ofNullable(User.builder().pw(pw).build()));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThatThrownBy(() -> userService.signin(signinRequestDto))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PASSWORD_NOT_MATCH.getMessage());
    }


}