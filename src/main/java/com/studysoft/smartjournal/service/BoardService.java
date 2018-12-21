package com.studysoft.smartjournal.service;

import com.studysoft.smartjournal.domain.*;
import com.studysoft.smartjournal.domain.enumeration.Type;
import com.studysoft.smartjournal.repository.DayRepository;
import com.studysoft.smartjournal.repository.DayTypeRepository;
import com.studysoft.smartjournal.repository.UserRepository;
import com.studysoft.smartjournal.security.SecurityUtils;
import com.studysoft.smartjournal.web.rest.errors.EntityNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BoardService {

    private final UserRepository userRepository;
    private final DayTypeRepository dayTypeRepository;
    private final DayRepository dayRepository;
    private final MessageSource messageSource;


    public BoardService(UserRepository userRepository,
                        DayTypeRepository dayTypeRepository,
                        DayRepository dayRepository,
                        MessageSource messageSource) {
        this.userRepository = userRepository;
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
            .orElseThrow(() -> new EntityNotFoundException("user", "no authorized user in current session"));
        board.setUser(user);
    }

    /**
     * Generate schedule for specified period and days of week
     *
     * @param dateFrom date from which generate schedule
     * @param dateTo date to which generate schedule
     * @param days days of week which include to schedule
     * @return the list of dates
     */
    public List<LocalDate> generateSchedule(LocalDate dateFrom, LocalDate dateTo, List<Integer> days) {

        List<LocalDate> dates = new ArrayList<>();
        Integer firstDay = dateFrom.getDayOfWeek().getValue();

        if (days.contains(firstDay)) {
            dates.add(dateFrom);
        }

        for (Integer day : days) {
            LocalDate originDateFrom = dateFrom;
            while (true) {
                LocalDate nextDay = dateFrom.with(TemporalAdjusters.next(DayOfWeek.of(day)));
                if (nextDay.isBefore(dateTo) || nextDay.equals(dateTo)){
                    dates.add(nextDay);
                    dateFrom = nextDay;
                } else {
                    break;
                }
            }
            dateFrom = originDateFrom;
        }

        Collections.sort(dates);
        return dates;
    }

    /**
     * Create days for students of current board of default {@link DayType} (DayType.SIMPLE)
     *
     * @param board the board to fill
     * @param students the students of current board
     * @param dates the dates of current board
     */
    public void fillBoard(Board board, List<Student> students, List<LocalDate> dates) {
        DayType dayType = new DayType() // create default dayType
            .type(Type.SIMPLE)
            .score(1.0)
            .description(messageSource.getMessage("smartjournalApp.board.defaultDescription",
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

}
