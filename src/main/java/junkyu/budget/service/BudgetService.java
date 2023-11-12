package junkyu.budget.service;

import junkyu.budget.domain.Budget;
import junkyu.budget.domain.User;
import junkyu.budget.dto.BudgetCreateRequestDto;
import junkyu.budget.dto.BudgetGetRequestDto;
import junkyu.budget.dto.BudgetGetResponseDto;
import junkyu.budget.enums.Category;
import junkyu.budget.exception.CustomException;
import junkyu.budget.exception.ErrorCode;
import junkyu.budget.repository.BudgetRepository;
import junkyu.budget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createBudget(BudgetCreateRequestDto budgetCreateRequestDto){
        User findUser = userRepository.findById(budgetCreateRequestDto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        budgetRepository.save(Budget.builder()
                        .user(findUser)
                        .category(Category.nameOf(budgetCreateRequestDto.getCategory()))
                        .startDate(LocalDate.of(budgetCreateRequestDto.getYear(), budgetCreateRequestDto.getMonth(), 1))
                        .amount(budgetCreateRequestDto.getAmount())
                        .build());
    }

    /**
     *  년, 월을 받아 해당 월의 카테고리 별 예산과 총액을 반환
     */
    public List<BudgetGetResponseDto> getBudget(BudgetGetRequestDto budgetGetRequestDto){
        //카테고리별 예산 조회
        List<BudgetGetResponseDto> dtoList = budgetRepository.findByBudgetGetRequestDto(budgetGetRequestDto);

        //모든 카테고리의 예산을 합하여 총액으로 입력
        dtoList.add(BudgetGetResponseDto.builder()
                .category("총액")
                .amount(dtoList.stream().mapToLong(BudgetGetResponseDto::getAmount).sum())
                .build());

        return dtoList;
    }

    /**
     *  총액을 입력받아 카테고리별 예산을 추천
     *  아이디 1인 유저의 이번달 예산 비율을 참고하여 추천
     */
    public List<BudgetGetResponseDto> recommendBudget(Long total){
        LocalDate now = LocalDate.now();

        BudgetGetRequestDto budgetGetRequestDto = BudgetGetRequestDto.builder()
                .userId(1L)
                .year(now.getYear())
                .month(now.getMonth().getValue())
                .build();

        //이번달 아이디가 1인 유저의 예산 비율 조회
        List<BudgetGetResponseDto> dtoList = budgetRepository.findByBudgetGetRequestDto(budgetGetRequestDto);

        Long findTotal = dtoList.stream().mapToLong(BudgetGetResponseDto::getAmount).sum();

        dtoList.add(BudgetGetResponseDto.builder()
                .category("총액")
                .amount(findTotal)
                .build());

        //조회한 비율대로 총액 분배
        dtoList.forEach(dto -> dto.recommend(findTotal, total));

        return dtoList;
    }

    public Long getTotal(Long userId){
        return budgetRepository.thisMonthTotalBudgetById(userId);
    }

}
