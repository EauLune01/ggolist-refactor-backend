package ggolist.refactor.place.repository.custom;

import ggolist.refactor.place.dto.result.PopupSummaryResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;

public interface PopupRepositoryCustom {
    Slice<PopupSummaryResult> findWeeklyPopups(LocalDate today, Pageable pageable);
}
