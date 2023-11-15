package org.crock.winterschool.kitchen;

import org.crock.winterschool.weekday.Weekday;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class Cook {
    private String name;
    private String surname;
    private Set<Weekday> workingDays;

    public boolean isWorkingDay(Weekday day) {
        return workingDays.contains(day);
    }

    public Cook(String name, String surname, Set<Weekday> weekdays) {
        Objects.requireNonNull(name, "У повара должно быть имя");
        this.name = name;
        Objects.requireNonNull(name, "У повара должна быть фамилия");
        this.surname = surname;
        if (weekdays == null || weekdays.size() == 0) {
            throw new IllegalArgumentException("Неверное количество дней работы");
        }
        weekdays.forEach(day -> Objects.requireNonNull(day, "день не может быть null"));
        workingDays = new HashSet<>(weekdays);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cook cook = (Cook) o;
        return Objects.equals(name, cook.name) && Objects.equals(workingDays, cook.workingDays) && Objects.equals(surname, cook.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, workingDays, surname);
    }

    @Override
    public String toString() {
        StringBuilder cookInfo = new StringBuilder(name + " " + surname + " Работает по дням:");
        for (Weekday day : workingDays) {
            cookInfo.append(" ").append(day);
        }
        return cookInfo.toString();
    }
}
