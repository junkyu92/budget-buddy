package junkyu.budget.service;

import junkyu.budget.domain.Spend;
import junkyu.budget.domain.User;
import junkyu.budget.dto.SpendGetRequestDto;
import junkyu.budget.dto.SpendGetResponseDto;
import junkyu.budget.dto.SpendRequestDto;
import junkyu.budget.enums.Category;
import junkyu.budget.exception.CustomException;
import junkyu.budget.exception.ErrorCode;
import junkyu.budget.repository.SpendRepository;
import junkyu.budget.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
class SpendServiceTest {

    @InjectMocks private SpendService spendService;

    @Mock private SpendRepository spendRepository;
    @Mock private UserRepository userRepository;

    @Test
    void 지출_생성(){
        SpendRequestDto spendRequestDto = SpendRequestDto.builder()
                .userId(999999999L)
                .category("식비")
                .build();
        User user = User.builder()
                .id(999999999L)
                .build();
        when(userRepository.findById(999999999L)).thenReturn(Optional.ofNullable(user));

        spendService.createSpend(spendRequestDto);

        verify(spendRepository).save(any(Spend.class));
    }

    @Test
    void 지출_생성_실패_존재하지_않는_유저() {
        SpendRequestDto spendRequestDto = SpendRequestDto.builder()
                .userId(999999999L)
                .category("식비")
                .build();
        when(userRepository.findById(999999999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> spendService.createSpend(spendRequestDto))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    void 지출_생성_실패_존재하지_않는_카테고리() {
        SpendRequestDto spendRequestDto = SpendRequestDto.builder()
                .userId(999999999L)
                .category("도리토스사먹음")
                .build();
        User user = User.builder()
                .id(999999999L)
                .build();
        when(userRepository.findById(999999999L)).thenReturn(Optional.ofNullable(user));

        assertThatThrownBy(() -> spendService.createSpend(spendRequestDto))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.CATEGORY_NOT_FOUND.getMessage());
    }

    @Test
    void 지출_수정(){
        SpendRequestDto spendRequestDto = SpendRequestDto.builder()
                .userId(999999999L)
                .category("교통비")
                .amount(3000L)
                .build();
        Spend spend = Spend.builder()
                .id(999999999L)
                .category(Category.FOOD)
                .amount(500L)
                .build();
        when(spendRepository.findById(999999999L)).thenReturn(Optional.ofNullable(spend));

        spendService.updateSpend(999999999L, spendRequestDto);

        assertThat(spend.getCategory()).isEqualTo(Category.TRANSPORTATION);
        assertThat(spend.getAmount()).isEqualTo(3000);
    }

    @Test
    void 지출_수정_실패_존재하지_않는_지출(){
        SpendRequestDto spendRequestDto = SpendRequestDto.builder()
                .userId(999999999L)
                .category("교통비")
                .amount(3000L)
                .build();
        when(spendRepository.findById(999999999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> spendService.updateSpend(999999999L, spendRequestDto))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.SPEND_NOT_FOUND.getMessage());
    }

    @Test
    void 지출_삭제(){
        Spend spend = Spend.builder()
                .id(999999999L)
                .category(Category.FOOD)
                .amount(500L)
                .build();
        when(spendRepository.findById(999999999L)).thenReturn(Optional.ofNullable(spend));

        spendService.deleteSpend(999999999L);

        verify(spendRepository).delete(any(Spend.class));
    }

    @Test
    void 지출_삭제_실패_존재하지_않는_지출(){
        when(spendRepository.findById(999999999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> spendService.deleteSpend(999999999L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.SPEND_NOT_FOUND.getMessage());
    }

    @Test
    void 지출_리스트조회(){
        SpendGetRequestDto spendGetRequestDto = SpendGetRequestDto.builder().build();
        List<SpendGetResponseDto> spendGetResponseDtoList = new ArrayList<>();
        spendGetResponseDtoList.add(SpendGetResponseDto.builder().category("식비").amount(10000L).build());
        spendGetResponseDtoList.add(SpendGetResponseDto.builder().category("교통비").amount(3000L).build());

        when(spendRepository.findBySpendListRequestDto(spendGetRequestDto)).thenReturn(spendGetResponseDtoList);

        List<SpendGetResponseDto> spendList = spendService.getSpendList(spendGetRequestDto);

        assertThat(spendList.size()).isEqualTo(3);
        assertThat(spendList.get(0).getCategory()).isEqualTo("식비");
        assertThat(spendList.get(0).getAmount()).isEqualTo(10000L);
        assertThat(spendList.get(1).getCategory()).isEqualTo("교통비");
        assertThat(spendList.get(1).getAmount()).isEqualTo(3000L);
        assertThat(spendList.get(2).getCategory()).isEqualTo("총액");
        assertThat(spendList.get(2).getAmount()).isEqualTo(13000L);
    }

    @Test
    void 지출_조회(){
        Spend spend = Spend.builder()
                .id(999999999L)
                .category(Category.FOOD)
                .amount(500L)
                .build();

        when(spendRepository.findById(999999999L)).thenReturn(Optional.ofNullable(spend));

        SpendGetResponseDto spendGetResponseDto = spendService.getSpend(999999999L);

        assertThat(spendGetResponseDto.getCategory()).isEqualTo(SpendGetResponseDto.from(spend).getCategory());
        assertThat(spendGetResponseDto.getAmount()).isEqualTo(SpendGetResponseDto.from(spend).getAmount());
    }

    @Test
    void 지출_조회_실패_존재하지_않는_지출(){
        when(spendRepository.findById(999999999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> spendService.getSpend(999999999L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.SPEND_NOT_FOUND.getMessage());
    }
}