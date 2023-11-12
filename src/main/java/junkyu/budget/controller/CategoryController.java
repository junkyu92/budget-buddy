package junkyu.budget.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import junkyu.budget.enums.Category;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
@Api(tags = "Category API", description = "카테고리 관련 API")
public class CategoryController {

    @GetMapping
    @ApiOperation(value = "카테고리 조회", notes = "전체 카테고리 리스트 조회 api입니다.")
    public ResponseEntity<List<String>> categoryList(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        List<String> categoryList = Arrays.stream(Category.values())
                .map(Category::getName)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(categoryList);
    }
}
