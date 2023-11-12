package junkyu.budget.repository;

import junkyu.budget.domain.Budget;
import junkyu.budget.repository.querydsl.BudgetQRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long>, BudgetQRepository {
}
