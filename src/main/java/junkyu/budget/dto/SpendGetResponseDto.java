package junkyu.budget.dto;

import junkyu.budget.domain.Spend;
import junkyu.budget.enums.Category;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SpendGetResponseDto {
    private String category;
    private Long amount;

    public SpendGetResponseDto(Category category, Long amount) {
        this.category = category.getName();
        this.amount = amount;
    }

    public static SpendGetResponseDto from(Spend spend){
        return SpendGetResponseDto.builder()
                .category(spend.getCategory().getName())
                .amount(spend.getAmount())
                .build();
    }

    public void getPercent(SpendGetResponseDto spendGetResponseDto){
        if(category.equals(spendGetResponseDto.getCategory())){
           amount = (long) ((double) spendGetResponseDto.getAmount() / (double) amount) * 100;
        }
    }
}
