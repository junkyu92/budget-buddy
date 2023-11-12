package junkyu.budget.dto;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class SignupDto {

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10글자여야 합니다.")
    private String account;

    @NotBlank
    @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력해 주세요.")
    private String pw;

}
