package ggolist.refactor.place.domain;

import ggolist.refactor.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BasePlace extends BaseTimeEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String intro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String thumbnail;

    @ElementCollection
    private List<String> images = new ArrayList<>();

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    protected BasePlace(String name, String address, String intro, Category category,
                        String thumbnail, List<String> images, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.address = address;
        this.intro = intro;
        this.category = category;
        this.thumbnail = thumbnail;
        this.images = (images != null) ? images : new ArrayList<>();
        this.startTime = startTime;
        this.endTime = endTime;
        this.likeCount = 0;
    }

    /********** 비즈니스 메서드 **********/

    public void setLikeCountForTesting(int likeCount) {
        this.likeCount = likeCount;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

}
