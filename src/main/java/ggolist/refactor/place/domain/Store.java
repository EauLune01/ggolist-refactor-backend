package ggolist.refactor.place.domain;

import ggolist.refactor.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stores")
public class Store extends BasePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User owner;

    @Column(nullable = false)
    private String number;

    private Store(User owner, String name, String address, String number, String intro,
                  Category category, String thumbnail, List<String> images,
                  LocalTime startTime, LocalTime endTime) {
        super(name, address, intro, category, thumbnail, images, startTime, endTime);
        this.owner = owner;
        this.number = number;
    }

    public static Store create(User owner, String name, String address, String number, String intro,
                               Category category, String thumbnail, List<String> images,
                               LocalTime startTime, LocalTime endTime) {
        return new Store(owner, name, address, number, intro, category, thumbnail, images, startTime, endTime);
    }
}
