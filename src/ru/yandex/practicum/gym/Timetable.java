package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, ArrayList<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> daySchedule = timetable.get(trainingSession.getDayOfWeek());

//        if (dayOfWeek == null) {
//            TreeMap<TimeOfDay, ArrayList<TrainingSession>> newValue = new TreeMap<>();
//            ArrayList<TrainingSession> trainingSession2 = new ArrayList<>();
//            trainingSession2.add(trainingSession);
//            TimeOfDay timeOfDay = trainingSession.getTimeOfDay();
//            newValue.put(timeOfDay, trainingSession2);
//            timetable.put(trainingSession.getDayOfWeek(), newValue);
//        } else {
//            timetable.put(trainingSession.getDayOfWeek(), daySchedule);
//        }

        if (daySchedule == null) {
            daySchedule = new TreeMap<>();
            timetable.put(trainingSession.getDayOfWeek(), daySchedule);
        }

        ArrayList<TrainingSession> listAtTime = daySchedule.get(trainingSession.getTimeOfDay());

        if (listAtTime == null) {
            listAtTime = new ArrayList<>();
            daySchedule.put(trainingSession.getTimeOfDay(), listAtTime);
        }

        listAtTime.add(trainingSession);

    }

    public Map<TimeOfDay, ArrayList<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        return timetable.getOrDefault(dayOfWeek, new TreeMap<>());
    }

    public ArrayList<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        //как реализовать, тоже непонятно, но сложность должна быть О(log(n))
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> trainingSessionsForDay = timetable.getOrDefault(dayOfWeek, new TreeMap<>());
        return trainingSessionsForDay.getOrDefault(timeOfDay, new ArrayList<>());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        HashMap<Coach, Integer> countsMap = new HashMap<>();

        // Твой тройной цикл для подсчета
        for (TreeMap<TimeOfDay, ArrayList<TrainingSession>> dayMap : timetable.values()) {
            for (ArrayList<TrainingSession> sessionsList : dayMap.values()) {
                for (TrainingSession session : sessionsList) {
                    Coach coach = session.getCoach();
                    countsMap.put(coach, countsMap.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : countsMap.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        Collections.sort(result);
        return result;
    }
}
