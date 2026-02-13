package ggolist.refactor.place.controller;

import ggolist.refactor.place.domain.Filter;
import ggolist.refactor.place.dto.response.EventSummaryResponse;
import ggolist.refactor.place.dto.result.EventSummaryResult;
import ggolist.refactor.place.service.EventQueryService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "이벤트 (Event)", description = "이벤트 필터 및 조회 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventQueryService eventQueryService;

    @Operation(
            summary = "필터 조건별 이벤트 목록 조회 (페이징)",
            description = """
                파라미터로 전달된 필터 기준에 따라 이벤트 목록을 Slice 형태로 조회합니다. (비로그인 접근 가능)
                - **POPULAR**: 현재 진행 중인 이벤트 중 좋아요 많은 순
                - **ONGOING**: 현재 진행 중인 이벤트 중 최신 등록 순
                - **CLOSING_TODAY**: 오늘 마감되는 이벤트 (마감 임박순)
                - **UPCOMING**: 오픈 예정인 이벤트 (시작 임박순)
                
                기본 페이지 크기는 10이며, `hasNext` 필드를 통해 다음 페이지 존재 여부를 확인하세요.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이벤트 목록 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 필터 값이 전달됨")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<SliceResponse<EventSummaryResponse>>> getEvents(
            @AuthenticationPrincipal User loginUser,
            @RequestParam(required = false, defaultValue = "ONGOING") Filter filter,
            @PageableDefault(size = 10) Pageable pageable) {
        Long userId = (loginUser != null) ? loginUser.getId() : null;
        Slice<EventSummaryResult> results = eventQueryService.getEventsByFilter(filter, userId, pageable);
        SliceResponse<EventSummaryResponse> response = SliceResponse.from(results.map(EventSummaryResponse::from));
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "이벤트 목록 조회 성공", response));
    }
}
