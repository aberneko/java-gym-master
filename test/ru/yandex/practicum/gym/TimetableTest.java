package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, ArrayList<TrainingSession>> mondaySession = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertNotNull(mondaySession, "За понедельник должен вернутся список");
        Assertions.assertEquals(1, mondaySession.size(), "в понедельник должна быть ровна одна тренировка");

        //Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, ArrayList<TrainingSession>> tuesdaySession = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySession.isEmpty(), "За вторник не должно быть занятий");
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, ArrayList<TrainingSession>> mondaySession = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySession.size(), "в понедельник должна быть ровна одна тренировка");

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        Map<TimeOfDay, ArrayList<TrainingSession>> thursdaySession = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySession.size(), "в четверг должно быть 2 занятия");
        NavigableSet<TimeOfDay> sortedKey = ((TreeMap<TimeOfDay, ArrayList<TrainingSession>>) thursdaySession).navigableKeySet();
        Assertions.assertEquals(13, sortedKey.first().getHours(), "Первая тренировка должна быть в 13 часов");
        Assertions.assertEquals(20, sortedKey.last().getHours(), "Вторая тренировка должна быть в 8 часов вечера");
        // Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, ArrayList<TrainingSession>> tuesdaySession = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySession.isEmpty(), "За вторник не должно быть занятий");
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        TimeOfDay thirteenTime = singleTrainingSession.getTimeOfDay();
        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        ArrayList<TrainingSession> mondaySessionFirst = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, thirteenTime);
        Assertions.assertEquals(1, mondaySessionFirst.size(), "В понедельник в 13 часов есть одно занятие");
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        TimeOfDay fourteenTime = new TimeOfDay(14, 0);
        ArrayList<TrainingSession> mondaySessionLast = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, fourteenTime);
        Assertions.assertTrue(mondaySessionLast.isEmpty());
    }

    @Test
    void testGetCountByCoachesSorting() {
        Timetable timetable = new Timetable();
        Coach coachHigh = new Coach("Иванов", "Иван", "Иванович");
        Coach coachLow = new Coach("Петров", "Петр", "Петрович");
        Group group = new Group("Бокс", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coachHigh, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachHigh, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachLow, DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));

        List<CounterOfTrainings> counts = timetable.getCountByCoaches();
        Assertions.assertEquals(2, counts.size());
        Assertions.assertEquals("Иванов", counts.get(0).getCoach().getSurname(), "Первым должен быть тренер с большим количеством занятий");
        Assertions.assertEquals(2, counts.get(0).getCount());
        Assertions.assertEquals(1, counts.get(1).getCount());
    }

    @Test
    void testMultipleGroupsAtSameTime() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TimeOfDay time = new TimeOfDay(10, 0);

        timetable.addNewTrainingSession(new TrainingSession(new Group("Йога", Age.ADULT, 60), coach, DayOfWeek.MONDAY, time));
        timetable.addNewTrainingSession(new TrainingSession(new Group("Танцы", Age.CHILD, 45), coach, DayOfWeek.MONDAY, time));

        ArrayList<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, time);

        Assertions.assertEquals(2, sessions.size(), "В одно время может быть несколько разных групп");
    }

    @Test
    void testEmptyTimetable() {
        Timetable timetable = new Timetable();

        Assertions.assertTrue(timetable.getCountByCoaches().isEmpty(), "Список тренеров должен быть пуст");
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY).isEmpty(), "Расписание на воскресенье должно быть пустым");
    }

}
