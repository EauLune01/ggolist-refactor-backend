package ggolist.refactor.favorite.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ggolist.refactor.favorite.dto.query.FavoriteItem;
import ggolist.refactor.favorite.dto.query.QFavoriteItem;
import ggolist.refactor.global.utils.SliceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ggolist.refactor.favorite.domain.QFavoriteEvent.favoriteEvent;
import static ggolist.refactor.favorite.domain.QFavoritePopup.favoritePopup;
import static ggolist.refactor.favorite.domain.QFavoriteStore.favoriteStore;
import static ggolist.refactor.place.domain.QEvent.event;
import static ggolist.refactor.place.domain.QPopup.popup;
import static ggolist.refactor.place.domain.QStore.store;


@Repository
@RequiredArgsConstructor
public class FavoriteQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Slice<FavoriteItem> findAllFavorites(Long userId, Pageable pageable) {
        List<FavoriteItem> stores = queryFactory
                .select(new QFavoriteItem(Expressions.asString("store"), store.id, store.name, store.likeCount, favoriteStore.createdAt))
                .from(favoriteStore)
                .join(favoriteStore.store, store)
                .where(favoriteStore.user.id.eq(userId))
                .fetch();

        List<FavoriteItem> events = queryFactory
                .select(new QFavoriteItem(Expressions.asString("event"),event.id, event.name, event.likeCount, favoriteEvent.createdAt))
                .from(favoriteEvent)
                .join(favoriteEvent.event, event)
                .where(favoriteEvent.user.id.eq(userId))
                .fetch();

        List<FavoriteItem> popups = queryFactory
                .select(new QFavoriteItem(Expressions.asString("popup"),popup.id, popup.name, popup.likeCount, favoritePopup.createdAt))
                .from(favoritePopup)
                .join(favoritePopup.popup, popup)
                .where(favoritePopup.user.id.eq(userId))
                .fetch();

        List<FavoriteItem> total = new ArrayList<>();
        total.addAll(stores);
        total.addAll(events);
        total.addAll(popups);

        total.sort(Comparator.comparing(FavoriteItem::getCreatedAt).reversed());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize() + 1, total.size());

        List<FavoriteItem> content = (start >= total.size()) ? new ArrayList<>() : new ArrayList<>(total.subList(start, end));

        return SliceUtils.checkLastPage(pageable, content);
    }
}

