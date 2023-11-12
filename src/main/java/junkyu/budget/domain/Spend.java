package junkyu.budget.domain;

import junkyu.budget.dto.SpendRequestDto;
import junkyu.budget.enums.Category;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Spend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Category category;

    private Long amount;
    private String memo;

    public void update(SpendRequestDto spendRequestDto) {
        category = Category.nameOf(spendRequestDto.getCategory());
        amount = spendRequestDto.getAmount();
        if (StringUtils.hasText(spendRequestDto.getMemo())) {
            memo = spendRequestDto.getMemo();
        }
    }
}
