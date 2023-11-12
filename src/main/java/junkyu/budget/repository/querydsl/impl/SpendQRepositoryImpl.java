package junkyu.budget.repository.querydsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import junkyu.budget.dto.SpendGetRequestDto;
import junkyu.budget.dto.SpendGetResponseDto;
import junkyu.budget.dto.SpendTodayResponseDto;
import junkyu.budget.repository.querydsl.SpendQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static junkyu.budget.domain.QSpend.*;

@Repository
@RequiredArgsConstructor
public class SpendQRepositoryImpl implements SpendQRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 카테고리별 합계 조회
     */
    @Override
    public List<SpendGetResponseDto> findBySpendListRequestDto(SpendGetRequestDto spendGetRequestDto) {
        return queryFactory.select(Projections.constructor(SpendGetResponseDto.class, spend.category, spend.amount.sum()))
                .from(spend)
                .where(searchCondition(spendGetRequestDto))
                .groupBy(spend.category)
                .fetch();
    }

    @Override
    public Long thisMonthTotalSpendById(Long userId) {
        return queryFactory.select(spend.amount.sum())
                .from(spend)
                .where(searchCondition2(userId))
                .groupBy(spend.user.id)
                .fetchOne();
    }

    @Override
    public List<SpendTodayResponseDto> getTodaySpendList(Long userId) {
        return queryFactory.select(Projections.constructor(SpendTodayResponseDto.class, spend))
                .from(spend)
                .where(searchCondition2(userId))
                .fetch();
    }

    public BooleanExpression userEq(Long userId){
        return spend.user.id.eq(userId);
    }

    public BooleanExpression dateEq(SpendGetRequestDto spendGetRequestDto){
        return spend.createdAt.year().eq(spendGetRequestDto.getYear())
                .and(spend.createdAt.month().eq(spendGetRequestDto.getMonth()));
    }

    public BooleanExpression thisMonth(){
        return spend.createdAt.year().eq(LocalDate.now().getYear())
                .and(spend.createdAt.month().eq(LocalDate.now().getMonthValue()));
    }
    public BooleanExpression searchCondition(SpendGetRequestDto spendGetRequestDto){
        return userEq(spendGetRequestDto.getUserId()).and(dateEq(spendGetRequestDto));
    }

    public BooleanExpression searchCondition2(Long userId){
        return userEq(userId).and(thisMonth());
    }

}
