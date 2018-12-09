package com.studysoft.smartjournal.service;

import com.studysoft.smartjournal.domain.Board;
import com.studysoft.smartjournal.repository.BoardRepository;
import com.studysoft.smartjournal.service.util.Constants;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
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
            board.setId(Long.parseLong(String.valueOf(item[0])));
            board.setName(String.valueOf(item[1]));
            if (item[2] != null) {
                board.setDescription(String.valueOf(item[2]));
            }
            if (item[3] != null) {
                board.setTotalScore(Double.parseDouble(String.valueOf(item[3])));
            }
            board.setCreatedBy(String.valueOf(item[4]));
            if (item[5] != null) {
                board.setCreated(LocalDateTime.parse(String.valueOf(item[5]),
                    DateTimeFormatter.ofPattern(Constants.DATE_TIME_PATTERN)).toInstant(
                    ZoneOffset.of(Constants.EASTERN_EUROPEAN_OFFSET)));
            }
            if (item[6] != null) {
                board.setUpdatedBy(String.valueOf(item[6]));
            }
            if (item[7] != null) {
                board.setUpdated(LocalDateTime.parse(String.valueOf(item[7]),
                    DateTimeFormatter.ofPattern(Constants.DATE_TIME_PATTERN)).toInstant(ZoneOffset.of(
                    Constants.EASTERN_EUROPEAN_OFFSET)));
            } else {
                board.setUpdated(null);
            }
            board.setDayTypes(null);
            board.setStudents(null);
            boards.add(board);
        }
        return boards;
    }
}
