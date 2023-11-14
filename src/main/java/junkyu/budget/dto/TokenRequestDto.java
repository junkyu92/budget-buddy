package junkyu.budget.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TokenRequestDto {

    @NotBlank(message = "리프레시 토큰을 필수로 입력해야 합니다.")
    private String refreshToken;

}
