package ggolist.refactor.place.controller;

import ggolist.refactor.place.dto.response.PopupSummaryResponse;
import ggolist.refactor.place.dto.result.PopupSummaryResult;
import ggolist.refactor.place.service.PopupQueryService;
import ggolist.refactor.global.dto.response.ApiResponse;
import ggolist.refactor.global.dto.response.SliceResponse;
import ggolist.refactor.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "팝업 (Popup)", description = "팝업 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/popup")
public class PopupController {

    private final PopupQueryService popupQueryService;

    @Operation(summary = "이번 주 팝업 스테이션 조회", description = "이번 주에 진행 중인 팝업 목록을 인기순으로 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<SliceResponse<PopupSummaryResponse>>> getWeeklyPopups(
            @AuthenticationPrincipal User loginUser,
            @PageableDefault(size = 10) Pageable pageable) {
        Long userId = (loginUser != null) ? loginUser.getId() : null;
        Slice<PopupSummaryResult> results = popupQueryService.getWeeklyPopups(userId, pageable);
        SliceResponse<PopupSummaryResponse> response = SliceResponse.from(results.map(PopupSummaryResponse::from));
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "이번 주 팝업 스테이션 조회 성공", response));
    }
}
