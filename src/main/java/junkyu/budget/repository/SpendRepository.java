package junkyu.budget.repository;

import junkyu.budget.domain.Spend;
import junkyu.budget.repository.querydsl.SpendQRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpendRepository extends JpaRepository<Spend, Long>, SpendQRepository {
}
