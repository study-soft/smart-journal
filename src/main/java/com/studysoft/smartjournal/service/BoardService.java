package com.studysoft.smartjournal.service;

import com.studysoft.smartjournal.domain.Board;
import com.studysoft.smartjournal.domain.User;
import com.studysoft.smartjournal.repository.BoardRepository;
import com.studysoft.smartjournal.repository.UserRepository;
import com.studysoft.smartjournal.security.SecurityUtils;
import com.studysoft.smartjournal.service.util.Constants;
import com.studysoft.smartjournal.web.rest.errors.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    /**
     * Maps raw result from method {@link BoardRepository#findAllByUserIsCurrentUser()} to the list of boards
     *
     * @return all boards belongs to current user
     */
    public List<Board> findAllByUserIsCurrentUser() {
        List<Object[]> raw = boardRepository.findAllByUserIsCurrentUser();
        List<Board> boards = new ArrayList<>();
        for (Object[] item : raw) {
            Board board = new Board();
            mapBoard(item, board);
            boards.add(board);
        }
        return boards;
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

    private void mapBoard(Object[] source, Board target) {
        target.setId(Long.parseLong(String.valueOf(source[0])));
        target.setName(String.valueOf(source[1]));
        if (source[2] != null) {
            target.setDescription(String.valueOf(source[2]));
        }
        if (source[3] != null) {
            target.setTotalScore(Double.parseDouble(String.valueOf(source[3])));
        }
        target.setCreatedBy(String.valueOf(source[4]));
        if (source[5] != null) {
            target.setCreated(LocalDateTime.parse(String.valueOf(source[5]),
                DateTimeFormatter.ofPattern(Constants.DATE_TIME_PATTERN)).toInstant(
                ZoneOffset.of(Constants.EASTERN_EUROPEAN_OFFSET)));
        }
        if (source[6] != null) {
            target.setUpdatedBy(String.valueOf(source[6]));
        }
        if (source[7] != null) {
            target.setUpdated(LocalDateTime.parse(String.valueOf(source[7]),
                DateTimeFormatter.ofPattern(Constants.DATE_TIME_PATTERN)).toInstant(ZoneOffset.of(
                Constants.EASTERN_EUROPEAN_OFFSET)));
        } else {
            target.setUpdated(null);
        }
        target.setDayTypes(null);
        target.setStudents(null);
    }
}
