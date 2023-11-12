package junkyu.budget.dto;

import junkyu.budget.enums.Category;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BudgetGetResponseDto {
    private String category;
    private Long amount;

    public BudgetGetResponseDto(Category category, Long amount) {
        this.category = category.getName();
        this.amount = amount;
    }

    public void recommend(Long findTotal, Long total){
        this.amount = (long) (this.amount * ((double)total/(double)findTotal));
    }
}
