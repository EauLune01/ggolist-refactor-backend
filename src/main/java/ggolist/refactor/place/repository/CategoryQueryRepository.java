package ggolist.refactor.place.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ggolist.refactor.place.domain.Category;
import ggolist.refactor.place.dto.query.CategoryItem;
import ggolist.refactor.place.dto.query.QCategoryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static ggolist.refactor.place.domain.QEvent.event;
import static ggolist.refactor.place.domain.QPopup.popup;
import static ggolist.refactor.place.domain.QStore.store;


@Repository
@RequiredArgsConstructor
public class CategoryQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<CategoryItem> findStoreItems(Category category, int limit) {
        return queryFactory
                .select(new QCategoryItem(
                        Expressions.asString("STORE"), store.id, store.name,
                        store.thumbnail, store.likeCount, Expressions.nullExpression(),
                        Expressions.nullExpression(), Expressions.nullExpression()))
                .from(store)
                .where(store.category.eq(category))
                .orderBy(store.likeCount.desc(), store.id.desc())
                .limit(limit)
                .fetch();
    }

    public List<CategoryItem> findPopupItems(Category category, LocalDate today, int limit) {
        return queryFactory
                .select(new QCategoryItem(
                        Expressions.asString("POPUP"), popup.id, popup.name,
                        popup.thumbnail, popup.likeCount, popup.description,
                        popup.startDate, popup.endDate))
                .from(popup)
                .where(popup.category.eq(category),
                        popup.startDate.loe(today),
                        popup.endDate.goe(today))
                .orderBy(popup.likeCount.desc(), popup.id.desc())
                .limit(limit)
                .fetch();
    }

    public List<CategoryItem> findEventItems(Category category, LocalDate today, int limit) {
        return queryFactory
                .select(new QCategoryItem(
                        Expressions.asString("EVENT"),
                        event.id,
                        event.name,
                        event.thumbnail,
                        event.likeCount,
                        event.description,
                        event.startDate,
                        event.endDate
                ))
                .from(event)
                .join(event.store, store)
                .where(
                        store.category.eq(category),
                        event.startDate.loe(today),
                        event.endDate.goe(today)
                )
                .orderBy(event.likeCount.desc(), event.id.desc())
                .limit(limit)
                .fetch();
    }
}
