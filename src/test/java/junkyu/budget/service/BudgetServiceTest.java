package junkyu.budget.service;

import junkyu.budget.domain.Budget;
import junkyu.budget.domain.User;
import junkyu.budget.dto.BudgetCreateRequestDto;
import junkyu.budget.dto.BudgetGetRequestDto;
import junkyu.budget.dto.BudgetGetResponseDto;
import junkyu.budget.exception.CustomException;
import junkyu.budget.exception.ErrorCode;
import junkyu.budget.repository.BudgetRepository;
import junkyu.budget.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @InjectMocks private BudgetService budgetService;

    @Mock private  BudgetRepository budgetRepository;
    @Mock private UserRepository userRepository;

    @Test
    void 예산_생성(){
        BudgetCreateRequestDto budgetCreateRequestDto = BudgetCreateRequestDto.builder()
                .userId(999999999L)
                .category("식비")
                .year(9999)
                .month(12)
                .amount(5000L)
                .build();
        User user = User.builder()
                .id(999999999L)
                .build();
        when(userRepository.findById(999999999L)).thenReturn(Optional.ofNullable(user));

        budgetService.createBudget(budgetCreateRequestDto);

        verify(budgetRepository).save(any(Budget.class));
    }

    @Test
    void 예산_생성_실패(){
        BudgetCreateRequestDto budgetCreateRequestDto = BudgetCreateRequestDto.builder()
                .userId(999999999L)
                .category("식비")
                .year(9999)
                .month(12)
                .amount(5000L)
                .build();
        when(userRepository.findById(999999999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.createBudget(budgetCreateRequestDto))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    void 예산_조회(){
        BudgetGetRequestDto budgetGetRequestDto = BudgetGetRequestDto.builder()
                .userId(999999999L)
                .year(9999)
                .month(12)
                .build();
        List<BudgetGetResponseDto> budgetGetResponseDtoList = new ArrayList<>();
        budgetGetResponseDtoList.add(BudgetGetResponseDto.builder().category("식비").amount(5000L).build());
        budgetGetResponseDtoList.add(BudgetGetResponseDto.builder().category("교통비").amount(3000L).build());

        when(budgetRepository.findByBudgetGetRequestDto(budgetGetRequestDto)).thenReturn(budgetGetResponseDtoList);

        List<BudgetGetResponseDto> result = budgetService.getBudget(budgetGetRequestDto);

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getCategory()).isEqualTo("식비");
        assertThat(result.get(0).getAmount()).isEqualTo(5000L);
        assertThat(result.get(1).getCategory()).isEqualTo("교통비");
        assertThat(result.get(1).getAmount()).isEqualTo(3000L);
        assertThat(result.get(2).getCategory()).isEqualTo("총액");
        assertThat(result.get(2).getAmount()).isEqualTo(8000L);
    }

    @Test
    void 예산_추천(){
        BudgetGetRequestDto budgetGetRequestDto = BudgetGetRequestDto.builder()
                .userId(999999999L)
                .year(9999)
                .month(12)
                .build();
        List<BudgetGetResponseDto> budgetGetResponseDtoList = new ArrayList<>();
        budgetGetResponseDtoList.add(BudgetGetResponseDto.builder().category("식비").amount(5000L).build());
        budgetGetResponseDtoList.add(BudgetGetResponseDto.builder().category("교통비").amount(3000L).build());

        when(budgetRepository.findByBudgetGetRequestDto(any())).thenReturn(budgetGetResponseDtoList);

        List<BudgetGetResponseDto> result = budgetService.recommendBudget(16000L);

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getCategory()).isEqualTo("식비");
        assertThat(result.get(0).getAmount()).isEqualTo(10000L);
        assertThat(result.get(1).getCategory()).isEqualTo("교통비");
        assertThat(result.get(1).getAmount()).isEqualTo(6000L);
        assertThat(result.get(2).getCategory()).isEqualTo("총액");
        assertThat(result.get(2).getAmount()).isEqualTo(16000L);
    }
}