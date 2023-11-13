package junkyu.budget.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import junkyu.budget.config.provider.JwtTokenProvider;
import junkyu.budget.dto.SpendGetResponseDto;
import junkyu.budget.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics")
@Api(tags = "Statistics API", description = "통계 관련 API")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/month")
    @ApiOperation(value = "지난 달 대비 소비율 조회", notes = "지난 달 대비 소비율 조회 api입니다.")
    public ResponseEntity<List<SpendGetResponseDto>> compareToLastMonth(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        List<SpendGetResponseDto> result = statisticsService.compareToLastMonth(jwtTokenProvider.getIdFromToken(token));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/dayOfWeek")
    @ApiOperation(value = "지난 요일 대비 소비율 조회", notes = "지난 요일 대비 소비율 조회 api입니다.")
    public ResponseEntity<List<SpendGetResponseDto>> compareToLastDayOfWeek(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        List<SpendGetResponseDto> result = statisticsService.compareToLastDayOfWeek(jwtTokenProvider.getIdFromToken(token));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/other-user")
    @ApiOperation(value = "다른 유저 대비 소비율 조회", notes = "다른 유저 대비 소비율 조회 api입니다.")
    public ResponseEntity<List<SpendGetResponseDto>> compareToOtherUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        List<SpendGetResponseDto> result = statisticsService.compareToOtherUser(jwtTokenProvider.getIdFromToken(token));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
