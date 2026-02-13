package ggolist.refactor.favorite.controller;

import ggolist.refactor.favorite.dto.response.FavoriteItemResponse;
import ggolist.refactor.favorite.dto.response.FavoriteResponse;
import ggolist.refactor.favorite.dto.result.FavoriteItemResult;
import ggolist.refactor.favorite.dto.result.FavoriteResult;
import ggolist.refactor.favorite.service.FavoriteQueryService;
import ggolist.refactor.favorite.service.FavoriteService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.web.PageableDefault;

@Tag(name = "즐겨찾기 (Favorite)", description = "가게, 이벤트, 팝업 즐겨찾기 등록 및 해제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final FavoriteQueryService favoriteQueryService;

    @Operation(
            summary = "가게 즐겨찾기 등록",
            description = "특정 가게를 내 즐겨찾기에 추가합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "즐겨찾기 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 등록된 장소"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "가게 또는 유저를 찾을 수 없음")
    })
    @PostMapping("/stores/{storeId}/favorites")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<FavoriteResponse>> addStoreFavorite(
            @AuthenticationPrincipal User loginUser,
            @PathVariable Long storeId) {
        favoriteService.addStoreFavorite(loginUser.getId(), storeId);
        FavoriteResult result = favoriteQueryService.getStoreFavoriteStatus(loginUser.getId(), storeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, 201, "가게 즐겨찾기 등록이 완료되었습니다.", FavoriteResponse.from(result)));
    }

    @Operation(
            summary = "가게 즐겨찾기 해제",
            description = "즐겨찾기 목록에서 특정 가게를 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "즐겨찾기 해제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "즐겨찾기 기록을 찾을 수 없음")
    })
    @DeleteMapping("/stores/{storeId}/favorites")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<FavoriteResponse>> deleteStoreFavorite(
            @AuthenticationPrincipal User loginUser,
            @PathVariable Long storeId) {
        favoriteService.deleteStoreFavorite(loginUser.getId(), storeId);
        FavoriteResult result = favoriteQueryService.getStoreFavoriteStatus(loginUser.getId(), storeId);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "가게 즐겨찾기가 해제되었습니다.", FavoriteResponse.from(result)));
    }

    @Operation(
            summary = "이벤트 즐겨찾기 등록",
            description = "특정 이벤트를 내 즐겨찾기에 추가합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이벤트 즐겨찾기 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 등록된 이벤트"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "이벤트를 찾을 수 없음")
    })
    @PostMapping("/events/{eventId}/favorites")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<FavoriteResponse>> addEventFavorite(
            @AuthenticationPrincipal User loginUser, @PathVariable Long eventId) {
        favoriteService.addEventFavorite(loginUser.getId(), eventId);
        FavoriteResult result = favoriteQueryService.getEventFavoriteStatus(loginUser.getId(), eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, 201, "이벤트 즐겨찾기 등록이 완료되었습니다.", FavoriteResponse.from(result)));
    }

    @Operation(
            summary = "이벤트 즐겨찾기 해제",
            description = "즐겨찾기 목록에서 특정 이벤트를 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이벤트 즐겨찾기 해제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "즐겨찾기 기록을 찾을 수 없음")
    })
    @DeleteMapping("/events/{eventId}/favorites")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<FavoriteResponse>> deleteEventFavorite(
            @AuthenticationPrincipal User loginUser, @PathVariable Long eventId) {
        favoriteService.deleteEventFavorite(loginUser.getId(), eventId);
        FavoriteResult result = favoriteQueryService.getEventFavoriteStatus(loginUser.getId(), eventId);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "이벤트 즐겨찾기가 해제되었습니다.", FavoriteResponse.from(result)));
    }

    @Operation(
            summary = "팝업 즐겨찾기 등록",
            description = "특정 팝업을 내 즐겨찾기에 추가합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "팝업 즐겨찾기 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 등록된 팝업"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "팝업을 찾을 수 없음")
    })
    @PostMapping("/popups/{popupId}/favorites")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<FavoriteResponse>> addPopupFavorite(
            @AuthenticationPrincipal User loginUser, @PathVariable Long popupId) {
        favoriteService.addPopupFavorite(loginUser.getId(), popupId);
        FavoriteResult result = favoriteQueryService.getPopupFavoriteStatus(loginUser.getId(), popupId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, 201, "팝업 즐겨찾기 등록이 완료되었습니다.", FavoriteResponse.from(result)));
    }

    @Operation(
            summary = "팝업 즐겨찾기 해제",
            description = "즐겨찾기 목록에서 특정 팝업을 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "팝업 즐겨찾기 해제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "즐겨찾기 기록을 찾을 수 없음")
    })
    @DeleteMapping("/popups/{popupId}/favorites")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<FavoriteResponse>> deletePopupFavorite(
            @AuthenticationPrincipal User loginUser, @PathVariable Long popupId) {
        favoriteService.deletePopupFavorite(loginUser.getId(), popupId);
        FavoriteResult result = favoriteQueryService.getPopupFavoriteStatus(loginUser.getId(), popupId);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "팝업 즐겨찾기가 해제되었습니다.", FavoriteResponse.from(result)));
    }

    @Operation(
            summary = "전체 즐겨찾기 목록 조회 (무한 스크롤)",
            description = "로그인한 유저가 찜한 가게, 이벤트, 팝업 목록을 통합하여 최신순으로 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "전체 즐겨찾기 목록 조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/favorites")
    public ResponseEntity<ApiResponse<SliceResponse<FavoriteItemResponse>>> getAllFavorites(
            @AuthenticationPrincipal User loginUser,
            @PageableDefault(size = 10) Pageable pageable) {
        Slice<FavoriteItemResult> results = favoriteQueryService.getAllFavorites(loginUser.getId(), pageable);
        SliceResponse<FavoriteItemResponse> response = SliceResponse.from(results.map(FavoriteItemResponse::from));
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "전체 즐겨찾기 목록 조회 성공", response));
    }
}
