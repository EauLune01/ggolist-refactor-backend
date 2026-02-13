package ggolist.refactor.init;

import ggolist.refactor.place.domain.Category;
import ggolist.refactor.place.domain.Event;
import ggolist.refactor.place.domain.Popup;
import ggolist.refactor.place.domain.Store;
import ggolist.refactor.place.repository.EventRepository;
import ggolist.refactor.place.repository.PopupRepository;
import ggolist.refactor.place.repository.StoreRepository;
import ggolist.refactor.user.domain.Role;
import ggolist.refactor.user.domain.User;
import ggolist.refactor.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final EventRepository eventRepository;
    private final PopupRepository popupRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

        if (userRepository.existsByEmail("user@test.com")) {
            return;
        }

        Faker faker = new Faker(new Locale("ko"));
        Category[] categories = Category.values();

        // 1. 일반 유저 생성 (User.create 메서드가 있다고 가정)
        // 만약 User.create 파라미터 순서가 다르면 맞춰서 수정해줘!
        List<Category> userInterests = new ArrayList<>(List.of(categories[0], categories[1], categories[2]));
        User user = User.create(
                "user@test.com",
                passwordEncoder.encode("user1234"),
                Role.USER,
                userInterests // 4번째 인자로 리스트 전달
        );
        userRepository.save(user);

        // 2. 상인 50명 + 가게 + 이벤트 생성
        for (int i = 0; i < 50; i++) {
            Category storeCategory = categories[i % categories.length];

            // 상인은 자기 가게 카테고리 하나만 리스트로 만들어서 전달
            User merchant = User.create(
                    "merchant" + i + "@test.com",
                    passwordEncoder.encode("123456"),
                    Role.MERCHANT,
                    new ArrayList<>(List.of(storeCategory))
            );
            userRepository.save(merchant);

            String address = (i % 2 == 0) ? "서울 마포구 홍대입구로 " + i + "길" : "서울 서대문구 신촌로 " + i + "길";

            // Store.create()
            Store store = Store.create(
                    merchant,
                    "가게 " + i,
                    address,
                    "010-" + faker.number().digits(4) + "-" + faker.number().digits(4),
                    "안녕하세요, " + storeCategory + " 전문점입니다.",
                    storeCategory,
                    "https://picsum.photos/seed/store" + i + "/300/200",
                    new ArrayList<>(List.of("https://picsum.photos/seed/store" + i + "-1/300/200")),
                    LocalTime.of(10, 0),
                    LocalTime.of(22, 0)
            );
            store.setLikeCountForTesting(faker.number().numberBetween(10, 300));
            storeRepository.save(store);

            // Event.create()
            for (int j = 1; j <= 2; j++) {
                LocalDate start = LocalDate.now().minusDays(faker.number().numberBetween(0, 5));
                LocalDate end = start.plusDays(faker.number().numberBetween(5, 10));

                Event event = Event.create(
                        store,
                        "이벤트 " + j + " - " + store.getName(),
                        "이벤트 상세 설명입니다.",
                        "한 줄 소개",
                        storeCategory,
                        "https://picsum.photos/seed/event" + i + "-" + j + "/300/200",
                        new ArrayList<>(),
                        start, end,
                        LocalTime.of(11, 0), LocalTime.of(21, 0)
                );
                event.setLikeCountForTesting(faker.number().numberBetween(0, 200));
                eventRepository.save(event);
            }

            if (i % 20 == 0) { em.flush(); em.clear(); }
        }

        // 3. 팝업 30개 생성
        List<User> merchants = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.MERCHANT).limit(30).toList();

        for (int k = 0; k < 30; k++) {
            Category cat = categories[k % categories.length];
            // Popup.create()
            Popup popup = Popup.create(
                    merchants.get(k),
                    "팝업 - " + cat.name(),
                    "서울 마포구 팝업대로 " + k,
                    "팝업 상세 설명",
                    "팝업 한 줄 소개",
                    cat,
                    "https://picsum.photos/seed/popup" + k + "/300/200",
                    new ArrayList<>(),
                    LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                    LocalTime.of(12, 0), LocalTime.of(20, 0)
            );
            popup.setLikeCountForTesting(faker.number().numberBetween(0, 200));
            popupRepository.save(popup);
        }

        em.flush(); em.clear();
    }
}
