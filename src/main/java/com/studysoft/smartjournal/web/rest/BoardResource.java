package com.studysoft.smartjournal.web.rest;

import com.studysoft.smartjournal.domain.Board;
import com.studysoft.smartjournal.domain.Day;
import com.studysoft.smartjournal.domain.DayType;
import com.studysoft.smartjournal.domain.Student;
import com.studysoft.smartjournal.domain.enumeration.Type;
import com.studysoft.smartjournal.repository.BoardRepository;
import com.studysoft.smartjournal.repository.DayRepository;
import com.studysoft.smartjournal.repository.DayTypeRepository;
import com.studysoft.smartjournal.repository.StudentRepository;
import com.studysoft.smartjournal.security.SecurityUtils;
import com.studysoft.smartjournal.service.BoardService;
import com.studysoft.smartjournal.web.rest.errors.BadRequestAlertException;
import com.studysoft.smartjournal.web.rest.errors.EntityNotFoundException;
import com.studysoft.smartjournal.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Board.
 */
@RestController
@RequestMapping("/api")
public class BoardResource {

    private final Logger log = LoggerFactory.getLogger(BoardResource.class);

    private static final String ENTITY_NAME = "board";

    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final StudentRepository studentRepository;

    public BoardResource(BoardRepository boardRepository,
                         BoardService boardService,
                         StudentRepository studentRepository) {
        this.boardRepository = boardRepository;
        this.boardService = boardService;
        this.studentRepository = studentRepository;
    }

    /**
     * POST  /boards : Create a new board. Create days for board if parameters <i>fromDate, toDate, days</i> exist
     *
     * @param board    the board to create
     * @param dateFrom date from which create days
     * @param dateTo   date to which create days
     * @param days     days of week for which create days
     * @return the ResponseEntity with status 201 (Created) and with body the new board, or with status 400 (Bad Request) if the board has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/boards")
    public ResponseEntity<Board> createBoard(@Valid @RequestBody Board board,
                                             @RequestParam(name = "from", required = false) LocalDate dateFrom,
                                             @RequestParam(name = "to", required = false) LocalDate dateTo,
                                             @RequestParam(name = "days", required = false) List<Integer> days) throws URISyntaxException {
        log.debug("REST request to save Board : {}", board);
        if (board.getId() != null) {
            throw new BadRequestAlertException("A new board cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Optional<Board> dbBoard = boardRepository.findByTitleForCurrentUser(board.getTitle());
        if (dbBoard.isPresent()) {
            throw new BadRequestAlertException("Board for such group and subject already exists", ENTITY_NAME, "boardExists");
        }

        boardService.setCurrentUser(board);
        Board result = boardService.saveBoard(board, dateFrom, dateTo, days);

        return ResponseEntity.created(new URI("/api/boards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boards : Updates an existing board.
     *
     * @param board the board to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated board,
     * or with status 400 (Bad Request) if the board is not valid,
     * or with status 500 (Internal Server Error) if the board couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/boards")
    public ResponseEntity<Board> updateBoard(@Valid @RequestBody Board board) throws URISyntaxException {
        log.debug("REST request to update Board : {}", board);
        if (board.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        boardService.setCurrentUser(board);
        Board result = boardRepository.save(board);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, board.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boards : get all the boards of current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of boards in body
     */
    @GetMapping("/boards")
    public List<Board> getAllBoards() {
        log.debug("REST request to get all Boards");
        return boardRepository.findAllByUserIsCurrentUser();
    }

    /**
     * GET  /boards/:id : get the "id" board.
     *
     * @param id the id of the board to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the board, or with status 404 (Not Found)
     */
    @SuppressWarnings("Duplicates")
    @GetMapping("/boards/{id}")
    public ResponseEntity<Board> getBoard(@PathVariable Long id) {
        log.debug("REST request to get Board : {}", id);
        Board board = boardRepository.findByIdEager(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME));
        if (board.getUser() != null &&
            !board.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))) {
            throw new BadRequestAlertException("Requested id does not belong to current user", ENTITY_NAME, "accessDenied");
        }
        return ResponseEntity.ok(board);
    }

    /**
     * DELETE  /boards/:id : delete the "id" board.
     *
     * @param id the id of the board to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        log.debug("REST request to delete Board : {}", id);

        Board board = boardRepository.findById(id).orElse(null);
        boardService.deleteBoard(board);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
