package ggolist.refactor.user.controller;

import ggolist.refactor.place.domain.Category;
import ggolist.refactor.global.dto.response.ApiResponse;
import ggolist.refactor.user.domain.User;
import ggolist.refactor.user.dto.command.CategoryToggleCommand;
import ggolist.refactor.user.dto.response.CategoryListResponse;
import ggolist.refactor.user.dto.result.CategoryListResult;
import ggolist.refactor.user.service.UserQueryService;
import ggolist.refactor.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 (User)", description = "사용자 프로필 및 관심 카테고리 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserQueryService userQueryService;

    @Operation(
            summary = "관심 카테고리 토글(설정/해제)",
            description = "카테고리 이름을 경로로 전달하여 해당 카테고리를 토글합니다. (최소 1개, 최대 3개)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 최신화 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "카테고리 개수 정책 위반 (1~3개)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "접근 권한 없음 (USER 전용)")
    })
    @PostMapping("/categories/{category}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<CategoryListResponse>> toggleUserCategory(
            @AuthenticationPrincipal User loginUser,
            @Parameter(description = "설정/해제할 카테고리 이름 (예: CAFE, FOOD, K_POP 등)", required = true)
            @PathVariable Category category) {
        userService.toggleCategory(CategoryToggleCommand.of(loginUser.getId(), category));
        CategoryListResult result = userQueryService.getUserCategories(loginUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "카테고리 최신화 성공", CategoryListResponse.from(result)));
    }

    @Operation(
            summary = "관심 카테고리 목록 조회",
            description = "현재 로그인한 사용자의 관심 카테고리 목록을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "접근 권한 없음 (USER 전용)")
    })
    @GetMapping("/categories")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<CategoryListResponse>> getUserCategories(
            @AuthenticationPrincipal User loginUser) {
        CategoryListResult result = userQueryService.getUserCategories(loginUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "카테고리 조회 성공",CategoryListResponse.from(result)));
    }
}
