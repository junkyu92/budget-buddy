package junkyu.budget.repository.querydsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import junkyu.budget.dto.BudgetGetRequestDto;
import junkyu.budget.dto.BudgetGetResponseDto;
import junkyu.budget.repository.querydsl.BudgetQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static junkyu.budget.domain.QBudget.*;

@Repository
@RequiredArgsConstructor
public class BudgetQRepositoryImpl implements BudgetQRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 카테고리별 합계 조회
     */
    @Override
    public List<BudgetGetResponseDto> findByBudgetGetRequestDto(BudgetGetRequestDto budgetGetRequestDto) {
        return queryFactory.select(Projections.constructor(BudgetGetResponseDto.class, budget.category, budget.amount.sum()))
                .from(budget)
                .where(searchCondition(budgetGetRequestDto))
                .groupBy(budget.category)
                .fetch();
    }

    @Override
    public Long thisMonthTotalBudgetById(Long userId) {
        return queryFactory.select(budget.amount.sum())
                .from(budget)
                .where(searchCondition2(userId))
                .groupBy(budget.user.id)
                .fetchOne();
    }

    public BooleanExpression userEq(Long id){
        return budget.user.id.eq(id);
    }

    public BooleanExpression dateEq(BudgetGetRequestDto budgetGetRequestDto){
        return budget.startDate.eq(LocalDate.of(budgetGetRequestDto.getYear(), budgetGetRequestDto.getMonth(), 1));
    }

    public BooleanExpression thisMonth(){
        return budget.startDate.year().eq(LocalDate.now().getYear())
                .and(budget.startDate.month().eq(LocalDate.now().getMonthValue()));
    }

    public BooleanExpression searchCondition(BudgetGetRequestDto budgetGetRequestDto){
        return userEq(budgetGetRequestDto.getUserId()).and(dateEq(budgetGetRequestDto));
    }

    public BooleanExpression searchCondition2(Long id){
        return userEq(id).and(thisMonth());
    }


}
