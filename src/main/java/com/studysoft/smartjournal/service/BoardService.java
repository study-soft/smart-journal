package com.studysoft.smartjournal.service;

import com.studysoft.smartjournal.domain.Board;
import com.studysoft.smartjournal.domain.User;
import com.studysoft.smartjournal.repository.UserRepository;
import com.studysoft.smartjournal.security.SecurityUtils;
import com.studysoft.smartjournal.web.rest.errors.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    private final UserRepository userRepository;

    public BoardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Set user from current session to field 'user' of board
     *
     * @param board board to set
     */
    public void setCurrentUser(Board board) {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElse(""))
            .orElseThrow(() -> new EntityNotFoundException("user", "no authorized user in current session"));
        board.setUser(user);
    }


}
