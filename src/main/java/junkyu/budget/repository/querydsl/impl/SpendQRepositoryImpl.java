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
    private final LocalDate localDate = LocalDate.now();

    /**
     * 카테고리별 합계 조회
     */
    @Override
    public List<SpendGetResponseDto> findBySpendListRequestDto(SpendGetRequestDto spendGetRequestDto) {
        return queryFactory.select(Projections.constructor(SpendGetResponseDto.class, spend.category, spend.amount.sum()))
                .from(spend)
                .where(userIdAndInputMonth(spendGetRequestDto))
                .groupBy(spend.category)
                .fetch();
    }

    @Override
    public Long thisMonthTotalSpendById(Long userId) {
        return queryFactory.select(spend.amount.sum())
                .from(spend)
                .where(userIdAndThisMonth(userId))
                .groupBy(spend.user.id)
                .fetchOne();
    }

    @Override
    public List<SpendTodayResponseDto> getTodaySpendList(Long userId) {
        return queryFactory.select(Projections.constructor(SpendTodayResponseDto.class, spend))
                .from(spend)
                .where(userIdAndThisMonth(userId))
                .fetch();
    }

    @Override
    public List<SpendGetResponseDto> lastMonthDayOfMonthSpend(Long userId) {
        return queryFactory.select(Projections.constructor(SpendGetResponseDto.class, spend.category, spend.amount.sum()))
                .from(spend)
                .where(userIdAndLastMonthDayOfMonth(userId))
                .groupBy(spend.category)
                .fetch();
    }

    @Override
    public List<SpendGetResponseDto> dayOfMonthSpend(Long userId) {
        return queryFactory.select(Projections.constructor(SpendGetResponseDto.class, spend.category, spend.amount.sum()))
                .from(spend)
                .where(userIdAndThisMonth(userId))
                .groupBy(spend.category)
                .fetch();
    }

    @Override
    public List<SpendGetResponseDto> lastDayOfWeekSpend(Long userId) {
        return queryFactory.select(Projections.constructor(SpendGetResponseDto.class, spend.category, spend.amount.sum()))
                .from(spend)
                .where(userIdAndLastDayOfWeek(userId))
                .groupBy(spend.category)
                .fetch();
    }

    @Override
    public List<SpendGetResponseDto> todaySpend(Long userId) {
        return queryFactory.select(Projections.constructor(SpendGetResponseDto.class, spend.category, spend.amount.sum()))
                .from(spend)
                .where(userIdAndToday(userId))
                .groupBy(spend.category)
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
        return spend.createdAt.year().eq(localDate.getYear())
                .and(spend.createdAt.month().eq(localDate.getMonthValue()));
    }
    public BooleanExpression lastMonthDayOfMonth(){
        LocalDate lastMonth = localDate.minusMonths(1);
        return spend.createdAt.year().eq(lastMonth.getYear())
                .and(spend.createdAt.month().eq(lastMonth.getMonthValue()))
                .and(spend.createdAt.dayOfMonth().between(1, lastMonth.getDayOfMonth()));
    }
    public BooleanExpression today(){
        return spend.createdAt.year().eq(localDate.getYear())
                .and(spend.createdAt.month().eq(localDate.getMonthValue()))
                .and(spend.createdAt.dayOfMonth().eq(localDate.getDayOfMonth()));
    }

    public BooleanExpression lastDayOfWeek(){
        return spend.createdAt.year().eq(localDate.getYear())
                .and(spend.createdAt.month().eq(localDate.getMonthValue()))
                .and(spend.createdAt.dayOfMonth().eq(localDate.getDayOfMonth()-7));
    }
    public BooleanExpression userIdAndInputMonth(SpendGetRequestDto spendGetRequestDto){
        return userEq(spendGetRequestDto.getUserId()).and(dateEq(spendGetRequestDto));
    }

    public BooleanExpression userIdAndThisMonth(Long userId){
        return userEq(userId).and(thisMonth());
    }

    public BooleanExpression userIdAndToday(Long userId){
        return userEq(userId).and(today());
    }
    public BooleanExpression userIdAndLastMonthDayOfMonth(Long userId){
        return userEq(userId).and(lastMonthDayOfMonth());
    }

    public BooleanExpression userIdAndLastDayOfWeek(Long userId){
        return userEq(userId).and(lastDayOfWeek());
    }

}
