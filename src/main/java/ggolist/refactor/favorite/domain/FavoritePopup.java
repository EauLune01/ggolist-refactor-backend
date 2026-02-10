package ggolist.refactor.favorite.domain;

import ggolist.refactor.basePlace.domain.Popup;
import ggolist.refactor.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "favorite_popups",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "popup_id"})}
)
public class FavoritePopup extends BaseFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popup_id", nullable = false)
    private Popup popup;

    private FavoritePopup(User user, Popup popup) {
        super(user);
        this.popup = popup;
    }

    public static FavoritePopup create(User user, Popup popup) {
        return new FavoritePopup(user, popup);
    }
}
