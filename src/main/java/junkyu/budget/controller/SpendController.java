package junkyu.budget.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import junkyu.budget.config.provider.JwtTokenProvider;
import junkyu.budget.dto.SpendGetRequestDto;
import junkyu.budget.dto.SpendGetResponseDto;
import junkyu.budget.dto.SpendRequestDto;
import junkyu.budget.service.SpendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/spends")
@Api(tags = "Spend API", description = "지출 관련 API")
@RequiredArgsConstructor
public class SpendController {

    private final SpendService spendService;
    private final JwtTokenProvider jwtTokenProvider;
    @PostMapping
    @ApiOperation(value = "지출 등록", notes = "지출등록 api입니다.")
    public ResponseEntity<Void> createSpend(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @Valid @RequestBody SpendRequestDto spendRequestDto) {
        spendRequestDto.setUserId(jwtTokenProvider.getIdFromToken(token));
        spendService.createSpend(spendRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "지출 수정", notes = "지출수정 api입니다.")
    public ResponseEntity<Void> updateSpend(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable("id") Long spendId, @Valid @RequestBody SpendRequestDto spendRequestDto) {
        spendRequestDto.setUserId(jwtTokenProvider.getIdFromToken(token));
        spendService.updateSpend(spendId, spendRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "지출 삭제", notes = "지출삭제 api입니다.")
    public ResponseEntity<Void> deleteSpend(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable("id") Long spendId) {
        spendService.deleteSpend(spendId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    @ApiOperation(value = "지출 리스트 조회", notes = "지출조회 api입니다.")
    public ResponseEntity<List<SpendGetResponseDto>> getSpendList(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @Valid SpendGetRequestDto spendGetRequestDto) {
        spendGetRequestDto.setUserId(jwtTokenProvider.getIdFromToken(token));
        List<SpendGetResponseDto> spendList = spendService.getSpendList(spendGetRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(spendList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "지출 조회", notes = "지출조회 api입니다.")
    public ResponseEntity<SpendGetResponseDto> getSpendList(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable("id") Long spendId) {
        SpendGetResponseDto spendGetResponseDto = spendService.getSpend(spendId);
        return ResponseEntity.status(HttpStatus.OK).body(spendGetResponseDto);
    }
}
