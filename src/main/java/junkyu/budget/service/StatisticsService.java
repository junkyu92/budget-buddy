package junkyu.budget.service;

import junkyu.budget.dto.SpendGetResponseDto;
import junkyu.budget.exception.CustomException;
import junkyu.budget.exception.ErrorCode;
import junkyu.budget.repository.SpendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final SpendRepository spendRepository;

    public List<SpendGetResponseDto> compareToLastMonth(Long userId){
        List<SpendGetResponseDto> lastMonthResult = spendRepository.lastMonthDayOfMonthSpend(userId);
        if(lastMonthResult.isEmpty()) throw new CustomException(ErrorCode.LAST_MONTH_NOT_FOUND);
        addTotal(lastMonthResult);

        List<SpendGetResponseDto> todayResult = spendRepository.dayOfMonthSpend(userId);
        addTotal(todayResult);

        todayResult.forEach(dto -> {
            lastMonthResult.forEach(dto::getPercent);
        });

        return todayResult;

    }

    public List<SpendGetResponseDto> compareToLastDayOfWeek(Long userId){
        List<SpendGetResponseDto> lastDayOfWeekResult = spendRepository.lastDayOfWeekSpend(userId);
        if(lastDayOfWeekResult.isEmpty()) throw new CustomException(ErrorCode.LAST_DAY_OF_WEEK_NOT_FOUND);
        addTotal(lastDayOfWeekResult);

        List<SpendGetResponseDto> todayResult = spendRepository.todaySpend(userId);
        addTotal(todayResult);

        todayResult.forEach(dto -> {
            lastDayOfWeekResult.forEach(dto::getPercent);
        });

        return todayResult;

    }
    public List<SpendGetResponseDto> compareToOtherUser(Long userId){
        List<SpendGetResponseDto> otherUserResult = spendRepository.dayOfMonthSpend(1L);
        addTotal(otherUserResult);

        List<SpendGetResponseDto> todayResult = spendRepository.dayOfMonthSpend(userId);
        addTotal(todayResult);

        todayResult.forEach(dto -> {
            otherUserResult.forEach(dto::getPercent);
        });

        return todayResult;

    }

    public void addTotal(List<SpendGetResponseDto> list){
        list.add(SpendGetResponseDto.builder()
                .category("총액")
                .amount(list.stream().mapToLong(SpendGetResponseDto::getAmount).sum())
                .build());
    }
}
