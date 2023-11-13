package junkyu.budget.repository.querydsl;

import junkyu.budget.dto.SpendGetRequestDto;
import junkyu.budget.dto.SpendGetResponseDto;
import junkyu.budget.dto.SpendTodayResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface SpendQRepository {
    List<SpendGetResponseDto> findBySpendListRequestDto(SpendGetRequestDto spendGetRequestDto);

    Long thisMonthTotalSpendById(Long id);

    List<SpendTodayResponseDto> getTodaySpendList(Long id);

    List<SpendGetResponseDto> lastMonthDayOfMonthSpend(Long userId);
    List<SpendGetResponseDto> dayOfMonthSpend(Long userId);
    List<SpendGetResponseDto> lastDayOfWeekSpend(Long userId);
    List<SpendGetResponseDto> todaySpend(Long userId);
}
