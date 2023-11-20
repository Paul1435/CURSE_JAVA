package org.crock.winterschool.menu;

import org.crock.winterschool.kitchen.Dish;
import org.crock.winterschool.kitchen.Kitchen;
import org.crock.winterschool.userintetrface.CreatingMenu;
import org.crock.winterschool.weekday.Weekday;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Menu {
    private CreatingMenu creatorMenu;
    private ArrayList<Dish> dishes;


    private void initMenu(Kitchen creator) {
        creatorMenu = creator;
    }

    public Menu(Weekday dayOfFeast, int countOfDishes, Kitchen creator) {
        initMenu(creator);
        dishes = creatorMenu.generateMenu(dayOfFeast, countOfDishes);
    }

    public List<Dish> createFeastMenu(Weekday dayOfFeast, int countOfDishes) {
        dishes = creatorMenu.generateMenu(dayOfFeast, countOfDishes);
        return new ArrayList<>(dishes);
    }

    public List<Dish> createSpecificFeastMenu(Weekday dayOfFeast, int countOfDishes, Predicate<Dish> dishPredicate) {
        dishes = (creatorMenu.generateSpecificMenu(dayOfFeast, countOfDishes, dishPredicate));
        return new ArrayList<>(dishes);
    }

    public Menu(Weekday dayOfFeast, int countOfDishes, Kitchen creator, Predicate<Dish> specific) {
        initMenu(creator);
        dishes = creatorMenu.generateSpecificMenu(dayOfFeast, countOfDishes, specific);
    }

    public List<Dish> getDishes() {
        return new ArrayList<>(dishes);
    }

    @Override
    public String toString() {
        StringBuilder menu = new StringBuilder("Меню:\n");
        for (Dish dish : dishes) {
            menu.append(dish).append('\n');
        }
        return menu.toString();
    }
}
