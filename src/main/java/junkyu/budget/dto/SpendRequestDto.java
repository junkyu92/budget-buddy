package junkyu.budget.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SpendRequestDto {

    @Setter
    private Long userId;

    @NotBlank(message = "카테고리는 필수 입력 항목입니다.")
    private String category;

    @NotNull(message = "금액은 필수 입력 항목입니다.")
    @Min(value = 100, message = "100 이상 입력해야 합니다.")
    private Long amount;

    private String memo;
}
