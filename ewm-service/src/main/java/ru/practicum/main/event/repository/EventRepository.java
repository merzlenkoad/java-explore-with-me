package ru.practicum.main.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    List<Event> getEventsByCategoryIdIn(List<Long> categories, Pageable pageable);


    List<Event> getEventsByStateIn(List<State> states, Pageable pageable);

    List<Event> getEventsByInitiatorIdIn(List<Long> users, Pageable pageable);

    List<Event> getEventsByCategoryIdInAndStateIn(List<Long> categories, List<State> states, Pageable pageable);

    List<Event> getEventsByInitiatorIdInAndStateIn(List<Long> users, List<State> states, Pageable pageable);

    List<Event> getEventsByInitiatorIdInAndCategoryIdIn(List<Long> users, List<Long> categories, Pageable pageable);

    List<Event> getEventsByInitiatorIdInAndStateInAndCategoryIdIn(List<Long> users, List<State> states, List<Long> categories, Pageable pageable);

    List<Event> getEventsByEventDateAfterAndEventDateBefore(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Event getEventByIdAndState(Long eventId, State state);

    List<Event> getEventsByCategoryIdInAndEventDateAfterAndEventDateBefore(
            List<Long> categories,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable);

    List<Event> getEventsByStateInAndEventDateAfterAndEventDateBefore(
            List<State> states,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable);

    List<Event> getEventsByInitiatorIdInAndEventDateAfterAndEventDateBefore(
            List<Long> users,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable);

    List<Event> getEventsByStateInAndCategoryIdInAndEventDateAfterAndEventDateBefore(
            List<State> states,
            List<Long> categories,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable);

    List<Event> getEventsByInitiatorIdInAndStateInAndEventDateAfterAndEventDateBefore(
            List<Long> users,
            List<State> states,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable);

    List<Event> getEventsByInitiatorIdInAndCategoryIdInAndEventDateAfterAndEventDateBefore(
            List<Long> users,
            List<Long> categories,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable);

    List<Event> getEventsByInitiatorIdInAndStateInAndCategoryIdInAndEventDateAfterAndEventDateBefore(
            List<Long> users,
            List<State> stateEnum,
            List<Long> categories,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable);


    List<Event> getEventsByInitiatorId(Long userId, Pageable pageable);

    Event findFirstByCategoryId(Long catId);

    Event getEventsByIdAndInitiatorId(Long eventId, Long userId);

    Event getEventsById(Long eventId);

    Set<Event> getEventsByIdIn(List<Long> events);

    List<Event> getEventsByInitiatorId(Long userId);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDateAvailableCategoryText(String state,
                                                                    List<Long> category,
                                                                    boolean paid,
                                                                    LocalDateTime time,
                                                                    String text,
                                                                    Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?4 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDateAvailableCategory(String state,
                                                                List<Long> category,
                                                                boolean paid,
                                                                LocalDateTime time,
                                                                Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDateAvailableText(String state,
                                                            boolean paid,
                                                            LocalDateTime time,
                                                            String text,
                                                            Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?3 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDateAvailable(String state,
                                                        boolean paid,
                                                        LocalDateTime time,
                                                        Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.event_date > ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDateCategoryText(String state,
                                                           List<Long> category,
                                                           boolean paid,
                                                           LocalDateTime time,
                                                           String text,
                                                           Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.event_date > ?4 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDateCategory(String state,
                                                       List<Long> category,
                                                       boolean paid,
                                                       LocalDateTime time,
                                                       Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.event_date > ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDateText(String state,
                                                   boolean paid,
                                                   LocalDateTime time,
                                                   String text,
                                                   Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.event_date > ?3 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDate(String state,
                                               boolean paid,
                                               LocalDateTime time,
                                               Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViewsAvailableCategoryText(String state,
                                                                List<Long> category,
                                                                boolean paid,
                                                                LocalDateTime time,
                                                                String text,
                                                                Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?4 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViewsAvailableCategory(String state,
                                                            List<Long> category,
                                                            boolean paid,
                                                            LocalDateTime time,
                                                            Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViewsAvailableText(String state,
                                                        boolean paid,
                                                        LocalDateTime time,
                                                        String text,
                                                        Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?4 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViewsAvailable(String state,
                                                    boolean paid,
                                                    LocalDateTime time,
                                                    Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.event_date > ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViewsCategoryText(String state,
                                                       List<Long> category,
                                                       boolean paid,
                                                       LocalDateTime time,
                                                       String text,
                                                       Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.event_date > ?4 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViewsCategory(String state,
                                                   List<Long> category,
                                                   boolean paid,
                                                   LocalDateTime time,
                                                   Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.event_date > ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViewsText(String state,
                                               boolean paid,
                                               LocalDateTime time,
                                               String text,
                                               Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.event_date > ?3 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViews(String state,
                                           boolean paid,
                                           LocalDateTime time,
                                           Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) ", nativeQuery = true)
    List<Event> getEventsNoPeriodAvailableCategoryText(String state,
                                                       List<Long> category,
                                                       boolean paid,
                                                       LocalDateTime time,
                                                       String text,
                                                       Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?4 ", nativeQuery = true)
    List<Event> getEventsNoPeriodAvailableCategory(String state,
                                                   List<Long> category,
                                                   boolean paid,
                                                   LocalDateTime time,
                                                   Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) ", nativeQuery = true)
    List<Event> getEventsNoPeriodAvailableText(String state,
                                               boolean paid,
                                               LocalDateTime time,
                                               String text,
                                               Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?4 ", nativeQuery = true)
    List<Event> getEventsNoPeriodAvailable(String state,
                                           boolean paid,
                                           LocalDateTime time,
                                           Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.event_date > ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) ", nativeQuery = true)
    List<Event> getEventsNoPeriodCategoryText(String state,
                                              List<Long> category,
                                              boolean paid,
                                              LocalDateTime time,
                                              String text,
                                              Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 and e.event_date > ?4 ", nativeQuery = true)
    List<Event> getEventsNoPeriodCategory(String state,
                                          List<Long> category,
                                          boolean paid,
                                          LocalDateTime time,
                                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.event_date > ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) ", nativeQuery = true)
    List<Event> getEventsNoPeriodText(String state,
                                      boolean paid,
                                      LocalDateTime time,
                                      String text,
                                      Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.event_date > ?3 ", nativeQuery = true)
    List<Event> getEventsNoPeriod(String state,
                                  boolean paid,
                                  LocalDateTime time,
                                  Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?4 and e.event_date <= ?5 " +
            "and upper(e.annotation) like upper(?6) or upper(e.description) like upper(?6) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDateAvailableCategoryText(String state,
                                                                  List<Long> category,
                                                                  boolean paid,
                                                                  LocalDateTime timeStart,
                                                                  LocalDateTime timeEnd,
                                                                  String text,
                                                                  Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?4 and e.event_date <= ?5 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDateAvailableCategory(String state,
                                                              List<Long> category,
                                                              boolean paid,
                                                              LocalDateTime timeStart,
                                                              LocalDateTime timeEnd,
                                                              Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDateAvailableText(String state,
                                                          boolean paid,
                                                          LocalDateTime timeStart,
                                                          LocalDateTime timeEnd,
                                                          String text,
                                                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >=  ?3 and e.event_date <= ?4 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDateAvailable(String state,
                                                      boolean paid,
                                                      LocalDateTime timeStart,
                                                      LocalDateTime timeEnd,
                                                      Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.event_date >= ?4 and e.event_date <= ?5 " +
            "and upper(e.annotation) like upper(?6) or upper(e.description) like upper(?6) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDateCategoryText(String state,
                                                         List<Long> category,
                                                         boolean paid,
                                                         LocalDateTime timeStart,
                                                         LocalDateTime timeEnd,
                                                         String text,
                                                         Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.event_date >= ?4 and e.event_date <= ?5 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDateCategory(String state,
                                                     List<Long> category,
                                                     boolean paid,
                                                     LocalDateTime timeStart,
                                                     LocalDateTime timeEnd,
                                                     Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.event_date >= ?3 and e.event_date < = ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDateText(String state,
                                                 boolean paid,
                                                 LocalDateTime timeStart,
                                                 LocalDateTime timeEnd,
                                                 String text,
                                                 Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDate(String state,
                                             boolean paid,
                                             LocalDateTime timeStart,
                                             LocalDateTime timeEnd,
                                             Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?4 and e.event_date <= ?5 " +
            "and upper(e.annotation) like upper(?6) or upper(e.description) like upper(?6) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViewsAvailableCategoryText(String state,
                                                              List<Long> category,
                                                              boolean paid,
                                                              LocalDateTime timeStart,
                                                              LocalDateTime timeEnd,
                                                              String text,
                                                              Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?4 and e.event_date <= ?5 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViewsAvailableCategory(String state,
                                                          List<Long> category,
                                                          boolean paid,
                                                          LocalDateTime timeStart,
                                                          LocalDateTime timeEnd,
                                                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViewsAvailableText(String state,
                                                      boolean paid,
                                                      LocalDateTime timeStart,
                                                      LocalDateTime timeEnd,
                                                      String text,
                                                      Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViewsAvailable(String state,
                                                  boolean paid,
                                                  LocalDateTime timeStart,
                                                  LocalDateTime timeEnd,
                                                  Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.event_date >= ?4 and e.event_date <= ?5 " +
            "and upper(e.annotation) like upper(?6) or upper(e.description) like upper(?6) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViewsCategoryText(String state,
                                                     List<Long> category,
                                                     boolean paid,
                                                     LocalDateTime timeStart,
                                                     LocalDateTime timeEnd,
                                                     String text,
                                                     Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.event_date >= ?4 and e.event_date <= ?5 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViewsCategory(String state,
                                                 List<Long> category,
                                                 boolean paid,
                                                 LocalDateTime timeStart,
                                                 LocalDateTime timeEnd,
                                                 Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViewsText(String state,
                                             boolean paid,
                                             LocalDateTime timeStart,
                                             LocalDateTime timeEnd,
                                             String text,
                                             Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViews(String state,
                                         boolean paid,
                                         LocalDateTime timeStart,
                                         LocalDateTime timeEnd,
                                         Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?4 and e.event_date <= ?5 " +
            "and upper(e.annotation) like upper(?6) or upper(e.description) like upper(?6) ", nativeQuery = true)
    List<Event> getEventsPeriodAvailableCategoryText(String state,
                                                     List<Long> category,
                                                     boolean paid,
                                                     LocalDateTime timeStart,
                                                     LocalDateTime timeEnd,
                                                     String text,
                                                     Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?4 and e.event_date <= ?5 ", nativeQuery = true)
    List<Event> getEventsPeriodAvailableCategory(String state,
                                                 List<Long> category,
                                                 boolean paid,
                                                 LocalDateTime timeStart,
                                                 LocalDateTime timeEnd,
                                                 Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) ", nativeQuery = true)
    List<Event> getEventsPeriodAvailableText(String state,
                                             boolean paid,
                                             LocalDateTime timeStart,
                                             LocalDateTime timeEnd,
                                             String text,
                                             Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?4 and e.event_date <= ?5 ", nativeQuery = true)
    List<Event> getEventsPeriodAvailable(String state,
                                         boolean paid,
                                         LocalDateTime timeStart,
                                         LocalDateTime timeEnd,
                                         Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.event_date >= ?4 and e.event_date <= ?5 " +
            "and upper(e.annotation) like upper(?6) or upper(e.description) like upper(?6) ", nativeQuery = true)
    List<Event> getEventsPeriodCategoryText(String state,
                                            List<Long> category,
                                            boolean paid,
                                            LocalDateTime timeStart,
                                            LocalDateTime timeEnd,
                                            String text,
                                            Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.paid=?3 " +
            "and e.event_date >= ?4 and e.event_date <= ?5 ", nativeQuery = true)
    List<Event> getEventsPeriodCategory(String state,
                                        List<Long> category,
                                        boolean paid,
                                        LocalDateTime timeStart,
                                        LocalDateTime timeEnd,
                                        Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) ", nativeQuery = true)
    List<Event> getEventsPeriodText(String state,
                                    boolean paid,
                                    LocalDateTime timeStart,
                                    LocalDateTime timeEnd,
                                    String text,
                                    Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.event_date >= ?3 and e.event_date <= ?4 ", nativeQuery = true)
    List<Event> getEventsPeriod(String state,
                                boolean paid,
                                LocalDateTime timeStart,
                                LocalDateTime timeEnd,
                                Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDateAvailableCategoryText(String state,
                                                                    List<Long> category,
                                                                    LocalDateTime time,
                                                                    String text,
                                                                    Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?3 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDateAvailableCategory(String state,
                                                                List<Long> category,
                                                                LocalDateTime time,
                                                                Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?2 " +
            "and upper(e.annotation) like upper(?3) or upper(e.description) like upper(?3) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDateAvailableText(String state,
                                                            LocalDateTime time,
                                                            String text,
                                                            Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?2 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDateAvailable(String state,
                                                        LocalDateTime time,
                                                        Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.event_date > ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDateCategoryText(String state,
                                                           List<Long> category,
                                                           LocalDateTime time,
                                                           String text,
                                                           Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.event_date > ?3 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDateCategory(String state,
                                                       List<Long> category,
                                                       LocalDateTime time,
                                                       Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.event_date > ?2 " +
            "and upper(e.annotation) like upper(?3) or upper(e.description) like upper(?3) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDateText(String state,
                                                   LocalDateTime time,
                                                   String text,
                                                   Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.event_date > ?2 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortEventDate(String state,
                                               LocalDateTime time,
                                               Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViewsAvailableCategoryText(String state,
                                                                List<Long> category,
                                                                LocalDateTime time,
                                                                String text,
                                                                Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?3 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViewsAvailableCategory(String state,
                                                            List<Long> category,
                                                            LocalDateTime time,
                                                            Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?2 " +
            "and upper(e.annotation) like upper(?3) or upper(e.description) like upper(?3) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViewsAvailableText(String state,
                                                        LocalDateTime time,
                                                        String text,
                                                        Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?2 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViewsAvailable(String state,
                                                    LocalDateTime time,
                                                    Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.event_date > ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViewsCategoryText(String state,
                                                       List<Long> category,
                                                       LocalDateTime time,
                                                       String text,
                                                       Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.event_date > ?3 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViewsCategory(String state,
                                                   List<Long> category,
                                                   LocalDateTime time,
                                                   Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.event_date > ?2 " +
            "and upper(e.annotation) like upper(?3) or upper(e.description) like upper(?3) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViewsText(String state,
                                               LocalDateTime time,
                                               String text,
                                               Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.event_date > ?2 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsNoPeriodSortViews(String state,
                                           LocalDateTime time,
                                           Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) ", nativeQuery = true)
    List<Event> getEventsNoPeriodAvailableCategoryText(String state,
                                                       List<Long> category,
                                                       LocalDateTime time,
                                                       String text,
                                                       Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?3 ", nativeQuery = true)
    List<Event> getEventsNoPeriodAvailableCategory(String state,
                                                   List<Long> category,
                                                   LocalDateTime time,
                                                   Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?2 " +
            "and upper(e.annotation) like upper(?3) or upper(e.description) like upper(?3) ", nativeQuery = true)
    List<Event> getEventsNoPeriodAvailableText(String state,
                                               LocalDateTime time,
                                               String text,
                                               Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date > ?2 ", nativeQuery = true)
    List<Event> getEventsNoPeriodAvailable(String state,
                                           LocalDateTime time,
                                           Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.event_date > ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) ", nativeQuery = true)
    List<Event> getEventsNoPeriodCategoryText(String state,
                                              List<Long> category,
                                              LocalDateTime time,
                                              String text,
                                              Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.event_date > ?3 ", nativeQuery = true)
    List<Event> getEventsNoPeriodCategory(String state,
                                          List<Long> category,
                                          LocalDateTime time,
                                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.event_date > ?2 " +
            "and upper(e.annotation) like upper(?3) or upper(e.description) like upper(?3) ", nativeQuery = true)
    List<Event> getEventsNoPeriodText(String state,
                                      LocalDateTime time,
                                      String text,
                                      Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.event_date > ?2 ", nativeQuery = true)
    List<Event> getEventsNoPeriod(String state,
                                  LocalDateTime time,
                                  Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDateAvailableCategoryText(String state,
                                                                  List<Long> category,
                                                                  LocalDateTime timeStart,
                                                                  LocalDateTime timeEnd,
                                                                  String text,
                                                                  Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDateAvailableCategory(String state,
                                                              List<Long> category,
                                                              LocalDateTime timeStart,
                                                              LocalDateTime timeEnd,
                                                              Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?2 and e.event_date <= ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDateAvailableText(String state,
                                                          LocalDateTime timeStart,
                                                          LocalDateTime timeEnd,
                                                          String text,
                                                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >=  ?2 and e.event_date <= ?3 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDateAvailable(String state,
                                                      LocalDateTime timeStart,
                                                      LocalDateTime timeEnd,
                                                      Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDateCategoryText(String state,
                                                         List<Long> category,
                                                         LocalDateTime timeStart,
                                                         LocalDateTime timeEnd,
                                                         String text,
                                                         Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDateCategory(String state,
                                                     List<Long> category,
                                                     LocalDateTime timeStart,
                                                     LocalDateTime timeEnd,
                                                     Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.event_date >= ?2 and e.event_date < = ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDateText(String state,
                                                 LocalDateTime timeStart,
                                                 LocalDateTime timeEnd,
                                                 String text,
                                                 Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.event_date >= ?2 and e.event_date <= ?3 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortEventDate(String state,
                                             LocalDateTime timeStart,
                                             LocalDateTime timeEnd,
                                             Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViewsAvailableCategoryText(String state,
                                                              List<Long> category,
                                                              LocalDateTime timeStart,
                                                              LocalDateTime timeEnd,
                                                              String text,
                                                              Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViewsAvailableCategory(String state,
                                                          List<Long> category,
                                                          LocalDateTime timeStart,
                                                          LocalDateTime timeEnd,
                                                          Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?2 and e.event_date <= ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViewsAvailableText(String state,
                                                      LocalDateTime timeStart,
                                                      LocalDateTime timeEnd,
                                                      String text,
                                                      Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?2 and e.event_date <= ?3 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViewsAvailable(String state,
                                                  LocalDateTime timeStart,
                                                  LocalDateTime timeEnd,
                                                  Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViewsCategoryText(String state,
                                                     List<Long> category,
                                                     LocalDateTime timeStart,
                                                     LocalDateTime timeEnd,
                                                     String text,
                                                     Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViewsCategory(String state,
                                                 List<Long> category,
                                                 LocalDateTime timeStart,
                                                 LocalDateTime timeEnd,
                                                 Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.event_date >= ?2 and e.event_date <= ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViewsText(String state,
                                             LocalDateTime timeStart,
                                             LocalDateTime timeEnd,
                                             String text,
                                             Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.event_date >= ?2 and e.event_date <= ?3 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> getEventsPeriodSortViews(String state,
                                         LocalDateTime timeStart,
                                         LocalDateTime timeEnd,
                                         Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) ", nativeQuery = true)
    List<Event> getEventsPeriodAvailableCategoryText(String state,
                                                     List<Long> category,
                                                     LocalDateTime timeStart,
                                                     LocalDateTime timeEnd,
                                                     String text,
                                                     Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?3 and e.event_date <= ?4 ", nativeQuery = true)
    List<Event> getEventsPeriodAvailableCategory(String state,
                                                 List<Long> category,
                                                 LocalDateTime timeStart,
                                                 LocalDateTime timeEnd,
                                                 Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?2 and e.event_date <= ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) ", nativeQuery = true)
    List<Event> getEventsPeriodAvailableText(String state,
                                             LocalDateTime timeStart,
                                             LocalDateTime timeEnd,
                                             String text,
                                             Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.paid=?2 " +
            "and e.participant_limit = 0 or e.participant_limit > e.confirmed_requests " +
            "and e.event_date >= ?3 and e.event_date <= ?4 ", nativeQuery = true)
    List<Event> getEventsPeriodAvailable(String state,
                                         LocalDateTime timeStart,
                                         LocalDateTime timeEnd,
                                         Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.event_date >= ?3 and e.event_date <= ?4 " +
            "and upper(e.annotation) like upper(?5) or upper(e.description) like upper(?5) ", nativeQuery = true)
    List<Event> getEventsPeriodCategoryText(String state,
                                            List<Long> category,
                                            LocalDateTime timeStart,
                                            LocalDateTime timeEnd,
                                            String text,
                                            Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.category_id in ?2 " +
            "and e.event_date >= ?3 and e.event_date <= ?4 ", nativeQuery = true)
    List<Event> getEventsPeriodCategory(String state,
                                        List<Long> category,
                                        LocalDateTime timeStart,
                                        LocalDateTime timeEnd,
                                        Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.event_date >= ?2 and e.event_date <= ?3 " +
            "and upper(e.annotation) like upper(?4) or upper(e.description) like upper(?4) ", nativeQuery = true)
    List<Event> getEventsPeriodText(String state,
                                    LocalDateTime timeStart,
                                    LocalDateTime timeEnd,
                                    String text,
                                    Pageable pageable);

    @Query(value = "select * from events as e " +
            "where e.state=?1 " +
            "and e.event_date >= ?2 and e.event_date <= ?3 ", nativeQuery = true)
    List<Event> getEventsPeriod(String state,
                                LocalDateTime timeStart,
                                LocalDateTime timeEnd,
                                Pageable pageable);
}