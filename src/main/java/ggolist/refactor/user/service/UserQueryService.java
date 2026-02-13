package ggolist.refactor.user.service;

import ggolist.refactor.global.exception.user.UserNotFoundException;
import ggolist.refactor.user.domain.User;
import ggolist.refactor.user.dto.result.CategoryListResult;
import ggolist.refactor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    public CategoryListResult getUserCategories(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        return CategoryListResult.of(user.getCategories());
    }
}
