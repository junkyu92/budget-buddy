package junkyu.budget.service;

import junkyu.budget.dto.SpendGetResponseDto;
import junkyu.budget.exception.CustomException;
import junkyu.budget.exception.ErrorCode;
import junkyu.budget.repository.SpendRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @InjectMocks private StatisticsService statisticsService;

    @Mock private SpendRepository spendRepository;

    @Test
    void 통계(){
        List<SpendGetResponseDto> lastMonth = new ArrayList<>();
        lastMonth.add(SpendGetResponseDto.builder().category("식비").amount(10000L).build());
        lastMonth.add(SpendGetResponseDto.builder().category("교통비").amount(3000L).build());

        List<SpendGetResponseDto> thisMonth = new ArrayList<>();
        thisMonth.add(SpendGetResponseDto.builder().category("식비").amount(5000L).build());
        thisMonth.add(SpendGetResponseDto.builder().category("교통비").amount(6000L).build());

        when(spendRepository.lastMonthDayOfMonthSpend(999999999L)).thenReturn(lastMonth);
        when(spendRepository.dayOfMonthSpend(999999999L)).thenReturn(thisMonth);

        List<SpendGetResponseDto> result = statisticsService.compareToLastMonth(999999999L);

        assertThat(result.get(0).getAmount()).isEqualTo(50);
        assertThat(result.get(1).getAmount()).isEqualTo(200);
        assertThat(result.get(2).getAmount()).isEqualTo(84);
    }

    @Test
    void 통계_실패_지난_달_데이터가_존재하지_않습니다(){
        List<SpendGetResponseDto> lastMonth = new ArrayList<>();

        when(spendRepository.lastMonthDayOfMonthSpend(999999999L)).thenReturn(lastMonth);

        assertThatThrownBy(() -> statisticsService.compareToLastMonth(999999999L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.LAST_MONTH_NOT_FOUND.getMessage());
    }

    @Test
    void 통계_실패_지난_요일_데이터가_존재하지_않습니다(){
        List<SpendGetResponseDto> lastDayOfWeek = new ArrayList<>();

        when(spendRepository.lastDayOfWeekSpend(999999999L)).thenReturn(lastDayOfWeek);

        assertThatThrownBy(() -> statisticsService.compareToLastDayOfWeek(999999999L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.LAST_DAY_OF_WEEK_NOT_FOUND.getMessage());
    }

    @Test
    void addTotal(){
        List<SpendGetResponseDto> lastMonth = new ArrayList<>();
        lastMonth.add(SpendGetResponseDto.builder().category("식비").amount(10000L).build());
        lastMonth.add(SpendGetResponseDto.builder().category("교통비").amount(3000L).build());

        statisticsService.addTotal(lastMonth);

        assertThat(lastMonth.get(2).getCategory()).isEqualTo("총액");
        assertThat(lastMonth.get(2).getAmount()).isEqualTo(13000L);
    }
}