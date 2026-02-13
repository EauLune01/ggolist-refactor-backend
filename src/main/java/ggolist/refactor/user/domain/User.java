package ggolist.refactor.user.domain;

import ggolist.refactor.place.domain.Category;
import ggolist.refactor.place.domain.Popup;
import ggolist.refactor.place.domain.Store;
import ggolist.refactor.favorite.domain.FavoriteEvent;
import ggolist.refactor.favorite.domain.FavoritePopup;
import ggolist.refactor.favorite.domain.FavoriteStore;
import ggolist.refactor.global.domain.BaseTimeEntity;
import ggolist.refactor.global.exception.user.InvalidCategorySizeException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET status = 'DELETED', deleted_at = CURRENT_TIMESTAMP WHERE user_id = ?")
@SQLRestriction("status = 'ACTIVE'")
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Category> categories = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String refreshToken;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Store store;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Popup> popups = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteStore> favoriteStores = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoritePopup> favoritePopups = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteEvent> favoriteEvents = new ArrayList<>();

    private User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = Status.ACTIVE;
        this.categories = new ArrayList<>();
    }

    public static User create(String email, String password, Role role, List<Category> categories) {
        User user = new User(email, password, role);
        user.setupInitialCategories(categories);
        return user;
    }

    /******************** 비즈니스 메서드 ********************/

    public void toggleCategory(Category category) {
        if (this.categories.contains(category)) {
            if (this.categories.size() <= 1) {
                throw new InvalidCategorySizeException("카테고리는 최소 1개 이상 선택해야 합니다.");
            }
            this.categories.remove(category);
        } else {
            if (this.categories.size() >= 3) {
                throw new InvalidCategorySizeException("카테고리는 최대 3개까지 선택 가능합니다.");
            }
            this.categories.add(category);
        }
    }

    private void setupInitialCategories(List<Category> initialCategories) {
        List<Category> targetCategories = (initialCategories != null) ?
                new ArrayList<>(initialCategories) : new ArrayList<>();
        validateInitialCategories(targetCategories);
        this.categories = targetCategories;
    }

    private void validateInitialCategories(List<Category> target) {
        if (this.role == Role.MERCHANT) {
            if (target.size() != 1) {
                throw new InvalidCategorySizeException("가게 카테고리는 반드시 1개를 설정해야 합니다.");
            }
        } else {
            if (target.isEmpty()) {
                throw new InvalidCategorySizeException("카테고리는 최소 1개 이상 선택해야 합니다.");
            }
            if (target.size() > 3) {
                throw new InvalidCategorySizeException("카테고리는 최대 3개까지 선택 가능합니다.");
            }
        }
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void invalidateRefreshToken() {
        this.refreshToken = null;
    }

    public boolean isMerchant() { return this.role == Role.MERCHANT; }
    public boolean isUser() { return this.role == Role.USER; }

    /******************** UserDetails 구현 ********************/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public String getPassword() { return this.password; }
    @Override public String getUsername() { return this.email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return this.status == Status.ACTIVE; }
}
