package junkyu.budget.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import junkyu.budget.config.provider.JwtTokenProvider;
import junkyu.budget.dto.SigninRequestDto;
import junkyu.budget.dto.SigninResponseDto;
import junkyu.budget.dto.SignupDto;
import junkyu.budget.dto.TokenRequestDto;
import junkyu.budget.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Api(tags = "User API", description = "사용자와 관련된 API")
@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입 api입니다.")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupDto signupDto) {
        userService.signup(signupDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    @ApiOperation(value = "로그인", notes = "로그인 api입니다.")
    public ResponseEntity<SigninResponseDto> signin(@Valid @RequestBody SigninRequestDto signinRequestDto) {
        SigninResponseDto signinResponseDto = userService.signin(signinRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(signinResponseDto);
    }

    @PostMapping("/token")
    @ApiOperation(value = "리프레시 토큰으로 액세스토큰 재발급", notes = "액세스토큰 재발급 api입니다.")
    public ResponseEntity<SigninResponseDto> token(@Valid @RequestBody TokenRequestDto tokenRequestDto){
        String refreshToken = tokenRequestDto.getRefreshToken();
        String accountFromToken = jwtTokenProvider.getAccountFromToken(refreshToken);
        SigninResponseDto signinResponseDto = userService.getAccessToken(refreshToken, accountFromToken);
        return ResponseEntity.status(HttpStatus.OK).body(signinResponseDto);
    }
}
