package ggolist.refactor.place.domain;

public enum Filter {
    POPULAR,         // 현재 진행 중인 이벤트 중 좋아요 순
    ONGOING,         // 현재 진행 중인 이벤트 중 최신 등록 순
    CLOSING_TODAY,   // 오늘 종료됨
    UPCOMING         // 앞으로 시작할 예정
}