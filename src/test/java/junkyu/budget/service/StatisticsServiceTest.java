package junkyu.budget.service;

import junkyu.budget.dto.SpendGetResponseDto;
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
    void addTotal(){
        List<SpendGetResponseDto> lastMonth = new ArrayList<>();
        lastMonth.add(SpendGetResponseDto.builder().category("식비").amount(10000L).build());
        lastMonth.add(SpendGetResponseDto.builder().category("교통비").amount(3000L).build());

        statisticsService.addTotal(lastMonth);

        assertThat(lastMonth.get(2).getCategory()).isEqualTo("총액");
        assertThat(lastMonth.get(2).getAmount()).isEqualTo(13000L);
    }
}