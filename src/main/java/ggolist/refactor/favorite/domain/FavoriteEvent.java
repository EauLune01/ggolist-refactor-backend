package ggolist.refactor.favorite.domain;

import ggolist.refactor.basePlace.domain.Event;
import ggolist.refactor.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "favorite_events",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "event_id"})}
)
public class FavoriteEvent extends BaseFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private FavoriteEvent(User user, Event event) {
        super(user);
        this.event = event;
    }

    public static FavoriteEvent create(User user, Event event) {
        return new FavoriteEvent(user, event);
    }
}
