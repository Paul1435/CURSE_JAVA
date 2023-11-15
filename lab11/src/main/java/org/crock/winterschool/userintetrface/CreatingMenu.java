package org.crock.winterschool.userintetrface;

import org.crock.winterschool.kitchen.Dish;
import org.crock.winterschool.weekday.Weekday;

import java.util.List;
import java.util.function.Predicate;

public interface CreatingMenu {
    List<Dish> generateMenu(Weekday dayOfFeast, int numberOfDishes);

    default List<Dish> generateSpecificMenu(Weekday dayOfFeast, int numberOfDishes, Predicate<Dish> specific) {
        List<Dish> mainMenu = generateMenu(dayOfFeast, numberOfDishes);
        return mainMenu.stream().filter(specific).toList();
    }
}
