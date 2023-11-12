package junkyu.budget.domain;

import junkyu.budget.enums.UserRole;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String account;
    private String pw;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "user")
    private List<Budget> budgetList;

    @OneToMany(mappedBy = "user")
    private List<Spend> spendList;

    @ColumnDefault("true")
    private Boolean notification;
}
