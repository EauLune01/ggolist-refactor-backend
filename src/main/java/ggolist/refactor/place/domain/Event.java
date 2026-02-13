package ggolist.refactor.place.domain;

import ggolist.refactor.favorite.domain.FavoriteEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "events")
public class Event extends BasePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteEvent> favorites = new ArrayList<>();

    private Event(Store store, String name, String description, String intro,
                  Category category, String thumbnail, List<String> images,
                  LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        super(name, store.getAddress(), intro, category, thumbnail, images, startTime, endTime);
        this.store = store;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Event create(Store store, String name, String description, String intro,
                               Category category, String thumbnail, List<String> images,
                               LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return new Event(store, name, description, intro, category, thumbnail, images, startDate, endDate, startTime, endTime);
    }

    /******************** 비즈니스 메서드 ********************/

    public boolean isExpired(LocalDate now) {
        return now.isAfter(this.endDate);
    }

    public boolean isOngoing(LocalDate now) {
        return (now.isEqual(this.startDate) || now.isAfter(this.startDate))
                && (now.isEqual(this.endDate) || now.isBefore(this.endDate));
    }
}
