package junkyu.budget.repository.querydsl;

import junkyu.budget.dto.SpendGetRequestDto;
import junkyu.budget.dto.SpendGetResponseDto;
import junkyu.budget.dto.SpendTodayResponseDto;

import java.util.List;

public interface SpendQRepository {
    List<SpendGetResponseDto> findBySpendListRequestDto(SpendGetRequestDto spendGetRequestDto);

    Long thisMonthTotalSpendById(Long id);

    List<SpendTodayResponseDto> getTodaySpendList(Long id);
}
