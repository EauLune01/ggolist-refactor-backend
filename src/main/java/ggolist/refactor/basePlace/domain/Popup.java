package ggolist.refactor.basePlace.domain;

import ggolist.refactor.favorite.domain.FavoritePopup;
import ggolist.refactor.user.domain.User;
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
@Table(name = "popups")
public class Popup extends BasePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "popup_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "popup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoritePopup> favorites = new ArrayList<>();

    private Popup(User owner, String name, String address, String description, String intro,
                  Category category, String thumbnail, List<String> images,
                  LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        super(name, address, intro, category, thumbnail, images, startTime, endTime);
        this.owner = owner;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Popup create(User owner, String name, String address, String description, String intro,
                               Category category, String thumbnail, List<String> images,
                               LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return new Popup(owner, name, address, description, intro, category, thumbnail, images, startDate, endDate, startTime, endTime);
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