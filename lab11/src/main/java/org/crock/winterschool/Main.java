package org.crock.winterschool;

import org.crock.winterschool.kitchen.Cook;
import org.crock.winterschool.kitchen.Dish;
import org.crock.winterschool.kitchen.Kitchen;
import org.crock.winterschool.menu.Menu;
import org.crock.winterschool.weekday.Weekday;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        HashMap<Cook, Set<Dish>> employees = new HashMap<>();
        Cook cookPaul = new Cook("Paul", "Shestakov", new HashSet<>(Arrays.asList(Weekday.MONDAY, Weekday.WEDNESDAY)));
        Cook cookPetr = new Cook("Petr", "Crocksov", new HashSet<>(Arrays.asList(Weekday.MONDAY, Weekday.SUNDAY)));
        HashMap<Cook, Set<String>> cooksMap = new HashMap<>();

        //Блюда, которые умеет готовить Павел
        Set<Dish> dishesPaul = new HashSet<>(Arrays.asList(
                new Dish("Блины", Dish.generateIngredients("Молоко", "Мука", "Яйцо"), Dish.Category.DESSERT, 75, 75, 275),
                new Dish("Каша", Dish.generateIngredients("Вода", "Овсянка"), Dish.Category.HOT, 12, 45, 300)));
        employees.put(cookPaul, dishesPaul);

        Set<Dish> dishesPetr = new HashSet<>(Arrays.asList(
                new Dish("Гречка", Dish.generateIngredients("Молоко", "Гречка"), Dish.Category.DESSERT, 75, 79, 275),
                new Dish("Суп", Dish.generateIngredients("Красная Вода", "мясо"), Dish.Category.HOT, 100, 90, 500)));
        employees.put(cookPetr, dishesPetr);
        Set<String> products = new HashSet<>();
        products.add("красная вода");
        products.add("мясо");
        products.add("Вода");
        products.add("Овсянка");
        Kitchen kingKitchen = new Kitchen(employees, products);
        Menu menu = new Menu(Weekday.MONDAY, 2, kingKitchen);
        System.out.println(menu);

    }
}