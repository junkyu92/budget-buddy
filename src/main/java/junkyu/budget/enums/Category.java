package junkyu.budget.enums;

import junkyu.budget.dto.BudgetGetResponseDto;
import junkyu.budget.exception.CustomException;
import junkyu.budget.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Category {

    FOOD("식비"),
    SNACKS("간식"),
    HOUSING("주거비"),
    TRANSPORTATION("교통비"),
    SELF_CARE("자기관리"),
    HOBBY_LEISURE("취미/여가"),
    HEALTH("의료/건강"),
    EDUCATION("교육/학습"),
    SHOPPING("쇼핑"),
    OTHER("기타");

    final private String name;

    public static Category nameOf(String name){
        return Arrays.stream(Category.values())
                .filter(e -> e.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
