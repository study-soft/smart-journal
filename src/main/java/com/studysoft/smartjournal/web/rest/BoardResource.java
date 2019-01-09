package com.studysoft.smartjournal.web.rest;

import com.studysoft.smartjournal.domain.Board;
import com.studysoft.smartjournal.domain.Day;
import com.studysoft.smartjournal.domain.DayType;
import com.studysoft.smartjournal.repository.BoardRepository;
import com.studysoft.smartjournal.repository.DayRepository;
import com.studysoft.smartjournal.repository.DayTypeRepository;
import com.studysoft.smartjournal.service.BoardService;
import com.studysoft.smartjournal.web.rest.errors.BadRequestAlertException;
import com.studysoft.smartjournal.web.rest.errors.EntityNotFoundException;
import com.studysoft.smartjournal.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.studysoft.smartjournal.service.util.Constants.*;

/**
 * REST controller for managing Board.
 */
@RestController
@RequestMapping("/api")
public class BoardResource {

    private final Logger log = LoggerFactory.getLogger(BoardResource.class);

    private final BoardRepository boardRepository;

    private final BoardService boardService;

    private final DayTypeRepository dayTypeRepository;

    private final DayRepository dayRepository;


    public BoardResource(BoardRepository boardRepository, BoardService boardService, DayTypeRepository dayTypeRepository,
                         DayRepository dayRepository) {

        this.boardRepository = boardRepository;
        this.boardService = boardService;
        this.dayTypeRepository = dayTypeRepository;
        this.dayRepository = dayRepository;
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
            throw new BadRequestAlertException("A new board cannot already have an ID", ENTITY_BOARD, "idexists");
        }

        Optional<Board> dbBoard = boardRepository.findByTitleForCurrentUser(board.getTitle());
        if (dbBoard.isPresent()) {
            throw new BadRequestAlertException("Board for such group and subject already exists", ENTITY_BOARD, "boardExists");
        }

        boardService.setCurrentUser(board);
        Board result = boardService.saveBoard(board, dateFrom, dateTo, days);

        return ResponseEntity.created(new URI("/api/boards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_BOARD, result.getId().toString()))
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
            throw new BadRequestAlertException("Invalid id", ENTITY_BOARD, "idnull");
        }

        boardService.setCurrentUser(board);
        Board result = boardRepository.save(board);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_BOARD, board.getId().toString()))
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
        Board board = boardRepository.findByIdEager(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_BOARD));
        boardService.checkCurrentUser(board);
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
        Board board = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_BOARD));
        boardService.checkCurrentUser(board);
        boardService.deleteBoard(board);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_BOARD, id.toString())).build();
    }

    /**
     * POST  /boards/:id/tasks : Create a new dayType.
     *
     * @param id      the id of the board
     * @param dayType the dayType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dayType, or with status 400 (Bad Request) if the dayType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/boards/{id}/tasks")
    public ResponseEntity<DayType> createDayType(@PathVariable Long id, @Valid @RequestBody DayType dayType) throws URISyntaxException {
        log.debug("REST request to save DayType : {}", dayType);
        if (dayType.getId() != null) {
            throw new BadRequestAlertException("A new dayType cannot already have an ID", ENTITY_DAYTYPE, "idexists");
        }
        dayType.setBoard(new Board().id(id));
        DayType result = dayTypeRepository.save(dayType);
        return ResponseEntity.created(new URI("/api/boards/" + id + "/tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_DAYTYPE, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boards/:id/tasks : Updates an existing dayType.
     *
     * @param id      the id of the board
     * @param dayType the dayType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dayType,
     * or with status 400 (Bad Request) if the dayType is not valid,
     * or with status 500 (Internal Server Error) if the dayType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/boards/{id}/tasks")
    public ResponseEntity<DayType> updateDayType(@PathVariable Long id, @Valid @RequestBody DayType dayType) throws URISyntaxException {
        log.debug("REST request to update DayType : {}", dayType);
        if (dayType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_DAYTYPE, "idnull");
        }
        dayType.setBoard(new Board().id(id));
        DayType result = dayTypeRepository.save(dayType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_DAYTYPE, dayType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boards/:id/tasks : get all the dayTypes.
     *
     * @param id the id of the board
     * @return the ResponseEntity with status 200 (OK) and the list of dayTypes in body
     */
    @GetMapping("/boards/{id}/tasks")
    public List<DayType> getAllDayTypes(@PathVariable Long id) {
        log.debug("REST request to get all DayTypes");
        return dayTypeRepository.findAllByBoardId(id);
    }

    /**
     * DELETE  /boards/:boardId/tasks/:dayTypeId : delete the "id" dayType.
     *
     * @param boardId   the id of the board
     * @param dayTypeId the id of the dayType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/boards/{boardId}/tasks/{dayTypeId}")
    public ResponseEntity<Void> deleteDayType(@PathVariable Long boardId, @PathVariable Long dayTypeId) {
        log.debug("REST request to delete DayType : {}", dayTypeId);
        boardService.checkDayType(dayTypeId, boardId);
        dayTypeRepository.deleteById(dayTypeId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_DAYTYPE, dayTypeId.toString())).build();
    }

    /**
     * POST  /boards/:id/days : Create a new days.
     *
     * @param id  the id of the board
     * @param day the day to create
     * @return the ResponseEntity with status 200 (Ok) and with body the new days, or with status 400 (Bad Request) if the day has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/boards/{id}/days")
    public ResponseEntity<List<Day>> createDay(@PathVariable Long id, @Valid @RequestBody Day day) throws URISyntaxException {
        log.debug("REST request to save Day : {}", day);
        if (day.getId() != null) {
            throw new BadRequestAlertException("A new day cannot already have an ID", ENTITY_DAY, "idexists");
        }
        List<Day> days = boardService.saveDays(id, day);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_DAY, days.stream().map(Day::getId).toString()))
            .body(days);
    }

    /**
     * PUT  /boards/:id/days : Updates an existing days.
     *
     * @param id  the id of the board
     * @param day the day to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated days,
     * or with status 400 (Bad Request) if the day is not valid,
     * or with status 500 (Internal Server Error) if the days couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/boards/{id}/days")
    public ResponseEntity<List<Day>> updateDays(@PathVariable Long id, @Valid @RequestBody Day day) throws URISyntaxException {
        log.debug("REST request to update Day : {}", day);
        if (day.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_DAY, "idnull");
        }
        List<Day> days = boardService.saveDays(id, day);
        List<Long> ids = days.stream().map(Day::getId).collect(Collectors.toList());
        System.out.println("ids: " + ids);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_DAY, ids.toString()))
            .body(days);
    }

    /**
     * PATCH /boards/:id/days : Updates the result of existing days
     *
     * @param id   the id of the board
     * @param days the days to update
     * @return the ResponseEntity with status 204 (No content)
     * or with status 400 (Bad Request) if the result of the day is not valid,
     * or with status 500 (Internal Server Error) if the result of the days couldn't be updated
     */
    @PatchMapping("/boards/{id}/days")
    public ResponseEntity<List<Day>> updateResult(@PathVariable Long id, @Valid @RequestBody List<Day> days) {
        log.debug("REST request to update Days results : {}", days);
        boardService.updateResult(days);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_DAY, days.stream()
                .map(Day::getId)
                .toString()))
            .body(days);
    }

    /**
     * DELETE  /boards/:boardId/days/:dayId : delete the day for each student in the group.
     *
     * @param boardId the id of the board
     * @param dayId   the id of the day to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/boards/{boardId}/days/{dayId}")
    public ResponseEntity<Void> deleteDay(@PathVariable Long boardId, @PathVariable Long dayId) {
        log.debug("REST request to delete Day : {}", dayId);
        boardService.checkDay(dayId, boardId);
        boardService.deleteDays(dayId);
        //TODO: deletion alert for day ids, not id
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_DAY, dayId.toString())).build();
    }
}
