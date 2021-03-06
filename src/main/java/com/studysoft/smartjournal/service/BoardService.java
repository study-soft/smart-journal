package com.studysoft.smartjournal.service;

import com.studysoft.smartjournal.domain.*;
import com.studysoft.smartjournal.domain.enumeration.Type;
import com.studysoft.smartjournal.repository.*;
import com.studysoft.smartjournal.security.SecurityUtils;
import com.studysoft.smartjournal.web.rest.errors.BadRequestAlertException;
import com.studysoft.smartjournal.web.rest.errors.EntityNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static com.studysoft.smartjournal.service.util.Constants.*;

@Service
public class BoardService {

    private final UserRepository userRepository;

    private final BoardRepository boardRepository;

    private final StudentRepository studentRepository;

    private final DayTypeRepository dayTypeRepository;

    private final DayRepository dayRepository;

    private final MessageSource messageSource;


    public BoardService(UserRepository userRepository, BoardRepository boardRepository, StudentRepository studentRepository,
                        DayTypeRepository dayTypeRepository, DayRepository dayRepository, MessageSource messageSource) {

        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.studentRepository = studentRepository;
        this.dayTypeRepository = dayTypeRepository;
        this.dayRepository = dayRepository;
        this.messageSource = messageSource;
    }

    /**
     * Set user from current session to field 'user' of board
     *
     * @param board board to set
     */
    public void setCurrentUser(Board board) {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElse(""))
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_USER, "no authorized user in current session"));
        board.setUser(user);
    }

    /**
     * Check that board belongs to user from current session
     *
     * @param board board to check
     */
    public void checkCurrentUser(Board board) {
        if (board.getUser() != null &&
            !board.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))) {
            throw new BadRequestAlertException("Requested id does not belong to current user", ENTITY_BOARD, "accessDenied");
        }
    }

    /**
     * Check if the dayType belongs to the board
     *
     * @param dayTypeId the id of the dayType
     * @param boardId   the id of the board
     */
    public void checkDayType(Long dayTypeId, Long boardId) {
        DayType dayType = dayTypeRepository.findById(dayTypeId).orElseThrow(() -> new EntityNotFoundException(ENTITY_DAYTYPE));
        if (!dayType.getBoard().getId().equals(boardId)) {
            throw new BadRequestAlertException("DayType with id '" + dayTypeId + "' does not belong to current board",
                ENTITY_DAYTYPE, "accessDenied");
        }
    }

    /**
     * Check if the day belongs to the board
     *
     * @param dayId   the id of the day
     * @param boardId the id of the board
     */
    public void checkDay(Long dayId, Long boardId) {
        Day day = dayRepository.findById(dayId).orElseThrow(() -> new EntityNotFoundException(ENTITY_DAY));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException(ENTITY_BOARD));
        if (!day.getStudent().getGroup().getId().equals(board.getGroup().getId())) {
            throw new BadRequestAlertException("Day with id '" + dayId + "' does not belong to current board",
                ENTITY_DAY, "accessDenied");
        }
    }

    /**
     * Save board and fill table groups_subjects in one transaction.
     * If parameters <i>dateFrom, dateTo, days</i> exist and at least one student in the group exist, generate schedule for this board
     *
     * @param board    the board to save
     * @param dateFrom date from which generate schedule
     * @param dateTo   date to which generate schedule
     * @param days     days of week which include to schedule
     * @return saved board
     */
    @Transactional
    public Board saveBoard(Board board, LocalDate dateFrom, LocalDate dateTo, List<Integer> days) {
        Board result = boardRepository.save(board);
        boardRepository.fillGroupsSubjects(result.getGroup().getId(), result.getSubject().getId());

        if (dateFrom != null && dateTo != null && days != null && !days.isEmpty()) {
            List<Student> students = studentRepository.findAllByGroupId(board.getGroup().getId());

            if (!students.isEmpty()) {
                List<LocalDate> dates = generateSchedule(dateFrom, dateTo, days);
                fillBoard(board, students, dates);
            }
        }

        return result;
    }

    /**
     * Generate schedule for specified period and days of week
     *
     * @param dateFrom date from which generate schedule
     * @param dateTo   date to which generate schedule
     * @param days     days of week which include to schedule
     * @return the list of dates
     */
    private List<LocalDate> generateSchedule(LocalDate dateFrom, LocalDate dateTo, List<Integer> days) {

        List<LocalDate> dates = new ArrayList<>();
        Integer firstDay = dateFrom.getDayOfWeek().getValue();

        if (days.contains(firstDay)) {
            dates.add(dateFrom);
        }

        for (Integer day : days) {
            LocalDate originDateFrom = dateFrom;
            while (true) {
                LocalDate nextDay = dateFrom.with(TemporalAdjusters.next(DayOfWeek.of(day)));
                if (nextDay.isBefore(dateTo) || nextDay.equals(dateTo)) {
                    dates.add(nextDay);
                    dateFrom = nextDay;
                } else {
                    break;
                }
            }
            dateFrom = originDateFrom;
        }

//        Collections.sort(dates);
        return dates;
    }

    /**
     * Create days for students of the current board of default {@link DayType} (DayType.SIMPLE).
     * Save it all to database in one transaction
     *
     * @param board    the board to fill
     * @param students the students of the current board
     * @param dates    the dates of the current board
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void fillBoard(Board board, List<Student> students, List<LocalDate> dates) {
        DayType dayType = new DayType() // create default dayType
            .type(Type.SIMPLE)
            .score(1.0)
            .name(messageSource.getMessage("board.create.dayType.name",
                null, LocaleContextHolder.getLocale()))
            .description(messageSource.getMessage("board.create.dayType.description",
                null, LocaleContextHolder.getLocale())) // TODO: i18n
//            .description("smartjournalApp.board.defaultDescription")
            .board(board);

        dayTypeRepository.save(dayType);

        students.forEach(student -> {
            dates.forEach(date -> {
                Day day = new Day()
                    .date(date)
                    .student(student)
                    .dayType(dayType);
                dayRepository.save(day);
            });
        });
    }

    /**
     * Save the same days for all the students of the board in one transaction
     *
     * @param boardId the id of board
     * @param day     the day to save
     * @return the list of saved days
     */
    @Transactional
    public List<Day> saveDays(Long boardId, Day day) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException(ENTITY_BOARD));
        List<Student> students = studentRepository.findAllByGroupId(board.getGroup().getId());
        List<Day> savedDays = new ArrayList<>();

        if (!students.isEmpty()) {
            students.forEach(student -> {
                day.setStudent(student);
                Day savedDay = dayRepository.save(day);
                savedDays.add(savedDay);
            });
        }

        return savedDays;
    }

    /**
     * Update the days
     *
     * @param days the days to update
     */
    @Transactional
    public void updateResult(List<Day> days) {
        if (days != null && !days.isEmpty()) {
            days.forEach(day -> dayRepository.updateDayResult(day.getId(), day.getResult()));
        }
    }

    /**
     * Delete the board and entry from groups_subjects in one transaction
     *
     * @param board the board to delete
     */
    @Transactional
    public void deleteBoard(Board board) {
        boardRepository.deleteById(board.getId());
        boardRepository.deleteGroupsSubjects(board.getGroup().getId(), board.getSubject().getId());
    }

    /**
     * Delete day for each student in the group with the same date as in the day with specified id
     *
     * @param dayId the id of fay
     */
    @Transactional
    public void deleteDays(Long dayId) {
        Day day = dayRepository.findById(dayId).orElseThrow(() -> new EntityNotFoundException(ENTITY_DAY));
        List<Student> students = studentRepository.findAllByGroupId(day.getStudent().getGroup().getId());
        students.forEach(student -> dayRepository.deleteByDateAndStudentId(day.getDate(), student.getId()));
    }

}
