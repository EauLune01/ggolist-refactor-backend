package ggolist.refactor.favorite.domain;

import ggolist.refactor.basePlace.domain.Store;
import ggolist.refactor.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "favorite_stores",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "store_id"})}
)
public class FavoriteStore extends BaseFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private FavoriteStore(User user, Store store) {
        super(user);
        this.store = store;
    }

    public static FavoriteStore create(User user, Store store) {
        return new FavoriteStore(user, store);
    }
}
