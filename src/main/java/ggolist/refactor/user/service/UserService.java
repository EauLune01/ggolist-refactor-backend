package ggolist.refactor.user.service;

import ggolist.refactor.global.exception.user.UserNotFoundException;
import ggolist.refactor.user.domain.User;
import ggolist.refactor.user.dto.command.CategoryToggleCommand;
import ggolist.refactor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void toggleCategory(CategoryToggleCommand command) {
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        user.toggleCategory(command.getCategory());
    }
}
