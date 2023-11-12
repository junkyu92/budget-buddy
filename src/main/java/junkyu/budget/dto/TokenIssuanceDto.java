package junkyu.budget.dto;

import junkyu.budget.domain.User;
import junkyu.budget.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenIssuanceDto {
    private Long id;
    private String account;
    private UserRole userRole;

    public static TokenIssuanceDto from(User user) {
        return TokenIssuanceDto.builder()
                .id(user.getId())
                .account(user.getAccount())
                .userRole(user.getUserRole())
                .build();
    }
}