package junkyu.budget.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    //GlobalException
    UNDEFINED_EXCEPTION(HttpStatus.BAD_REQUEST, "알 수 없는 오류입니다."),

    //User Exception
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    DUPLICATED_ACCOUNT(HttpStatus.BAD_REQUEST, "이미 가입된 계정입니다."),

    //Token Exception
    MISSING_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "요청헤더에 Access Token을 찾을 수 없습니다."),
    NOT_BEARER_TOKEN(HttpStatus.UNAUTHORIZED, "Bearer Token을 입력해주세요."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다."),

    //Enum Exception
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리가 존재하지 않습니다."),

    //Spend Exception
    SPEND_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 지출이 존재하지 않습니다."),

    //Statistics Exception
    LAST_MONTH_NOT_FOUND(HttpStatus.NOT_FOUND, "지난 달 데이터가 존재하지 않습니다."),
    LAST_DAY_OF_WEEK_NOT_FOUND(HttpStatus.NOT_FOUND, "지난 요일 데이터가 존재하지 않습니다.");


    private final HttpStatus status;
    private final String message;
}