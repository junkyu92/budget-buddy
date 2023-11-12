package junkyu.budget.dto;

import junkyu.budget.enums.Category;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SpendTodayResponseDto {
    private String category;
    private Long amount;
    private LocalDate createdAt;
    private String memo;

    public SpendTodayResponseDto(Category category, Long amount, LocalDate createdAt, String memo) {
        this.category = category.getName();
        this.amount = amount;
        this.createdAt = createdAt;
        this.memo = memo;
    }

}
