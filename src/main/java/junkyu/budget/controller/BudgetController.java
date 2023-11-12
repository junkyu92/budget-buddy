package junkyu.budget.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import junkyu.budget.config.provider.JwtTokenProvider;
import junkyu.budget.dto.BudgetCreateRequestDto;
import junkyu.budget.dto.BudgetGetRequestDto;
import junkyu.budget.dto.BudgetGetResponseDto;
import junkyu.budget.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
@Api(tags = "Budget API", description = "예산 관련 API")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final JwtTokenProvider jwtTokenProvider;
    @PostMapping
    @ApiOperation(value = "예산 등록", notes = "예산등록 api입니다.")
    public ResponseEntity<Void> createBudget(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @Valid @RequestBody BudgetCreateRequestDto budgetCreateRequestDto) {
        budgetCreateRequestDto.setUserId(jwtTokenProvider.getIdFromToken(token));
        budgetService.createBudget(budgetCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @ApiOperation(value = "예산 조회", notes = "년도와 월을 입력하여 해당 월의 예산을 조회하는 api입니다.")
    public ResponseEntity<List<BudgetGetResponseDto>> getBudget(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @Valid BudgetGetRequestDto budgetGetRequestDto) {
        budgetGetRequestDto.setUserId(jwtTokenProvider.getIdFromToken(token));
        List<BudgetGetResponseDto> dtoList = budgetService.getBudget(budgetGetRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }
    @GetMapping("recommend")
    @ApiOperation(value = "예산 추천", notes = "총액을 입력하고 카테고리별 금액을 추천받는 api입니다.")
    public ResponseEntity<List<BudgetGetResponseDto>> recommendBudget(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam("total") Long total) {
        List<BudgetGetResponseDto> dtoList = budgetService.recommendBudget(total);
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }
}
