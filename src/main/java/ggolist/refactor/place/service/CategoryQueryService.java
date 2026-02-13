package ggolist.refactor.place.service;

import ggolist.refactor.global.utils.SliceUtils;
import ggolist.refactor.place.domain.Category;
import ggolist.refactor.place.dto.query.CategoryItem;
import ggolist.refactor.place.dto.result.AllCategoryListResult;
import ggolist.refactor.place.dto.result.CategoryContentResult;
import ggolist.refactor.place.repository.CategoryQueryRepository;
import ggolist.refactor.favorite.repository.FavoriteEventRepository;
import ggolist.refactor.favorite.repository.FavoritePopupRepository;
import ggolist.refactor.favorite.repository.FavoriteStoreRepository;
import ggolist.refactor.user.domain.Role;
import ggolist.refactor.user.domain.User;
import ggolist.refactor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryService {

    private final UserRepository userRepository;
    private final CategoryQueryRepository categoryQueryRepository;
    private final FavoriteStoreRepository favoriteStoreRepository;
    private final FavoritePopupRepository favoritePopupRepository;
    private final FavoriteEventRepository favoriteEventRepository;

    public AllCategoryListResult getAllCategoryFeed(Long userId) {

        User loginUser = (userId != null) ? userRepository.findById(userId).orElse(null) : null;
        LocalDate today = LocalDate.now();
        List<Category> orderedCategories = orderCategories(loginUser);

        Set<Long> myStoreIds = getFavoriteIds(loginUser, "STORE");
        Set<Long> myPopupIds = getFavoriteIds(loginUser, "POPUP");
        Set<Long> myEventIds = getFavoriteIds(loginUser, "EVENT");

        List<CategoryContentResult> blocks = new ArrayList<>();

        for (Category category : orderedCategories) {
            List<CategoryItem> allItems = new ArrayList<>();
            allItems.addAll(categoryQueryRepository.findStoreItems(category, 4));
            allItems.addAll(categoryQueryRepository.findPopupItems(category, today, 4));
            allItems.addAll(categoryQueryRepository.findEventItems(category, today, 4));

            allItems.forEach(item -> {
                boolean liked = switch (item.getType()) {
                    case "STORE" -> myStoreIds.contains(item.getId());
                    case "POPUP" -> myPopupIds.contains(item.getId());
                    case "EVENT" -> myEventIds.contains(item.getId());
                    default -> false;
                };
                item.updateLiked(liked);
            });

            List<CategoryItem> sortedTop4 = allItems.stream()
                    .sorted(Comparator.comparing(CategoryItem::getLikeCount, Comparator.reverseOrder())
                            .thenComparing(CategoryItem::getId, Comparator.reverseOrder()))
                    .limit(4)
                    .toList();

            blocks.add(CategoryContentResult.of(category, sortedTop4));
        }

        return AllCategoryListResult.of(blocks);
    }

    public Slice<CategoryItem> getSingleCategoryFeed(Long userId, Category category, Pageable pageable) {
        LocalDate today = LocalDate.now();

        List<CategoryItem> allItems = new ArrayList<>();
        allItems.addAll(categoryQueryRepository.findStoreItems(category, Integer.MAX_VALUE));
        allItems.addAll(categoryQueryRepository.findPopupItems(category, today, Integer.MAX_VALUE));
        allItems.addAll(categoryQueryRepository.findEventItems(category, today, Integer.MAX_VALUE));

        User user = (userId != null) ? userRepository.findById(userId).orElse(null) : null;
        Set<Long> myStoreIds = getFavoriteIds(user, "STORE");
        Set<Long> myPopupIds = getFavoriteIds(user, "POPUP");
        Set<Long> myEventIds = getFavoriteIds(user, "EVENT");

        allItems.forEach(item -> {
            boolean liked = switch (item.getType()) {
                case "STORE" -> myStoreIds.contains(item.getId());
                case "POPUP" -> myPopupIds.contains(item.getId());
                case "EVENT" -> myEventIds.contains(item.getId());
                default -> false;
            };
            item.updateLiked(liked);
        });

        List<CategoryItem> sortedItems = allItems.stream()
                .sorted(Comparator.comparing(CategoryItem::getLikeCount, Comparator.reverseOrder())
                        .thenComparing(CategoryItem::getId, Comparator.reverseOrder()))
                .toList();

        int start = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();

        if (start >= sortedItems.size()) {
            return new SliceImpl<>(new ArrayList<>(), pageable, false);
        }

        int end = Math.min(start + pageSize + 1, sortedItems.size());

        List<CategoryItem> content = new ArrayList<>(sortedItems.subList(start, end));

        return SliceUtils.checkLastPage(pageable, content);
    }

    /******************** Helper Method ********************/

    private List<Category> orderCategories(User user) {
        List<Category> all = new ArrayList<>(Arrays.asList(Category.values()));

        if (user == null) {
            all.sort(Comparator.comparing(Enum::name));
            return all;
        }

        List<Category> userCategories = user.getCategories();

        all.sort((c1, c2) -> {
            int p1 = userCategories.indexOf(c1);
            int p2 = userCategories.indexOf(c2);

            // 둘 다 유저의 관심사인 경우 -> 알파벳 순서대로 정렬
            if (p1 != -1 && p2 != -1) return c1.name().compareTo(c2.name());

            // c1만 관심사인 경우 -> c1을 위로
            if (p1 != -1) return -1;

            // c2만 관심사인 경우 -> c2를 위로
            if (p2 != -1) return 1;

            // 둘 다 관심사가 아닌 경우 -> 알파벳 순
            return c1.name().compareTo(c2.name());
        });

        return all;
    }

    private Set<Long> getFavoriteIds(User user, String type) {
        if (user == null) return Collections.emptySet();
        return switch (type) {
            case "STORE" -> favoriteStoreRepository.findStoreIdsByUserId(user.getId());
            case "POPUP" -> favoritePopupRepository.findPopupIdsByUserId(user.getId());
            case "EVENT" -> favoriteEventRepository.findEventIdsByUserId(user.getId());
            default -> Collections.emptySet();
        };
    }
}
