package junkyu.budget.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SpendGetRequestDto {

    @Setter
    private Long userId;

    @NotNull(message = "년도는 필수 입력 항목입니다.")
    @Min(value = 2023, message = "2023 ~ 2100 사이로 입력해야 합니다.")
    @Max(value = 2100, message = "2023 ~ 2100 사이로 입력해야 합니다.")
    private Integer year;

    @NotNull(message = "월은 필수 입력 항목입니다.")
    @Min(value = 1, message = "1 ~ 12 사이로 입력해야 합니다.")
    @Max(value = 12, message = "1 ~ 12 사이로 입력해야 합니다.")
    private Integer month;
}
