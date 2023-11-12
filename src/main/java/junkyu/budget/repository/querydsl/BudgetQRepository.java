package junkyu.budget.repository.querydsl;

import junkyu.budget.dto.BudgetGetRequestDto;
import junkyu.budget.dto.BudgetGetResponseDto;

import java.util.List;

public interface BudgetQRepository {
    List<BudgetGetResponseDto> findByBudgetGetRequestDto(BudgetGetRequestDto budgetGetRequestDto);

    Long thisMonthTotalBudgetById(Long userId);
}
