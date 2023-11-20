package org.crock.winterschool.userintetrface;

import org.crock.winterschool.kitchen.Dish;
import org.crock.winterschool.weekday.Weekday;

import java.util.*;
import java.util.function.Predicate;

public interface CreatingMenu {
    ArrayList<Dish> generateMenu(Weekday dayOfFeast, int numberOfDishes);

    ArrayList<Dish> generateSpecificMenu(Weekday dayOfFeast, int numberOfDishes, Predicate<Dish> specific);
}
