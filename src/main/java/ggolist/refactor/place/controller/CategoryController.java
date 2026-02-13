package ggolist.refactor.place.controller;

import ggolist.refactor.place.domain.Category;
import ggolist.refactor.place.dto.query.CategoryItem;
import ggolist.refactor.place.dto.response.AllCategoryListResponse;
import ggolist.refactor.place.dto.result.AllCategoryListResult;
import ggolist.refactor.place.service.CategoryQueryService;
import ggolist.refactor.global.dto.response.ApiResponse;
import ggolist.refactor.global.dto.response.SliceResponse;
import ggolist.refactor.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Category", description = "카테고리 피드 및 상세 조회 관련 API")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryQueryService categoryQueryService;

    @Operation(
            summary = "전체 카테고리 메인 피드 조회",
            description = """
                메인 화면에 필요한 카테고리별 요약 피드를 조회합니다.
                - **비로그인**: 전체 카테고리 알파벳순 반환
                - **로그인(USER)**: 관심 카테고리(최대 3개) 우선 정렬 후 나머지 알파벳순
                - **로그인(MERCHANT)**: 본인 가게 카테고리 우선 정렬 후 나머지 알파벳순
                - 각 카테고리별로 좋아요 순 상위 4개 아이템을 포함합니다.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "전체 카테고리 목록 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 자격 증명이 유효하지 않음")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<AllCategoryListResponse>> getCategoryFeed(
            @AuthenticationPrincipal User loginUser) {
        Long userId = (loginUser != null) ? loginUser.getId() : null;
        AllCategoryListResult result = categoryQueryService.getAllCategoryFeed(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "전체 카테고리 목록 조회 성공", AllCategoryListResponse.from(result)));
    }

    @Operation(
            summary = "단일 카테고리 상세 피드 조회 (무한 스크롤)",
            description = """
            특정 카테고리의 모든 콘텐츠(가게, 이벤트, 팝업)를 **무한 스크롤** 방식으로 조회합니다.
            - **정렬**: 좋아요 순(likeCount) 내림차순, ID 내림차순
            - **페이징**: `Slice` 형식을 사용하여 `hasNext`가 `true`일 때 다음 페이지를 요청하세요.
            - **필터링**: 현재 진행 중인 이벤트와 팝업만 노출됩니다.
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 상세 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 유저 또는 카테고리를 찾을 수 없음")
    })
    @GetMapping("/{category}")
    public ResponseEntity<ApiResponse<SliceResponse<CategoryItem>>> getCategoryFeedByCategory(
            @AuthenticationPrincipal User loginUser,
            @PathVariable Category category,
            @PageableDefault(size = 10) Pageable pageable) {
        Long userId = (loginUser != null) ? loginUser.getId() : null;
        Slice<CategoryItem> results = categoryQueryService.getSingleCategoryFeed(userId, category, pageable);
        SliceResponse<CategoryItem> response = SliceResponse.from(results);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "카테고리 상세 조회 성공", response));
    }
}