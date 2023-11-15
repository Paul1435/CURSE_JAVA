package org.crock.winterschool.kitchen;

import org.crock.winterschool.weekday.Weekday;
import org.crock.winterschool.userintetrface.CreatingMenu;
import org.crock.winterschool.userintetrface.ManagingCooks;

import java.util.*;
import java.util.stream.Collectors;

public class Kitchen implements ManagingCooks, CreatingMenu {
    private HashMap<Cook, Set<Dish>> cooksDishesMap;
    private Set<String> products; // Продукты, которые есть на кухне

    public Kitchen(Kitchen copy) {
        this(copy.cooksDishesMap, copy.products);
    }

    public Kitchen(HashMap<Cook, Set<Dish>> cooksDishesMap, Set<String> products) {
        this(products);
        Objects.requireNonNullElse(cooksDishesMap, new HashMap<>());
        this.cooksDishesMap = new HashMap<>(cooksDishesMap);
    }

    public Kitchen(Set<String> products) {
        cooksDishesMap = new HashMap<>();
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Не переданы продукты");
        }
        this.products = new HashSet<>(products);
    }

    private Set<Cook> workingCooks(Weekday dayOfFest) {
        Objects.requireNonNull(dayOfFest, "Не задан день пира");
        if (cooksDishesMap.isEmpty()) {
            throw new ArrayStoreException("На кухне никто не работает");
        }
        Set<Cook> workingCooks = new HashSet<>();
        for (Cook cook : cooksDishesMap.keySet()) {
            if (cook.isWorkingDay(dayOfFest)) {
                workingCooks.add(cook);
            }
        }
        if (workingCooks.isEmpty()) {
            throw new ArrayStoreException("На кухне никто не работает в этот день");
        }
        return workingCooks;
    }

    @Override
    public List<Dish> generateMenu(Weekday dayOfFeast, int numberOfDishes) {
        List<Dish> possibleDishes = new ArrayList<>();
        Set<Cook> workingCooks = workingCooks(dayOfFeast);

        for (Cook cook : workingCooks) {
            Set<Dish> availableDishes = cooksDishesMap.get(cook);
            if (availableDishes != null) {
                possibleDishes.addAll(availableDishes);
            }
        }
        products = products.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        possibleDishes.removeIf(dish ->
                dish.getIngredients().stream()
                        .map(String::toLowerCase)
                        .anyMatch(ingredient -> !products.contains(ingredient.toLowerCase()))
        );

        // Сортируем блюда по оценкам
        possibleDishes.sort(Dish::compareTo);

        // Ограничиваем количество блюд
        return possibleDishes.subList(0, Math.min(possibleDishes.size(), numberOfDishes));
    }

    @Override
    public void dismissCook(Cook cook) {
        cooksDishesMap.remove(cook);
    }

    @Override
    public void hireCook(Cook cook, Set<Dish> dishes) {
        if (cook == null) {
            throw new NullPointerException("Был передан null объект вместо повара");
        }
        if (dishes == null || dishes.size() == 0) {
            throw new IllegalArgumentException("Не были переданы блюда данного повара.");
        }
        dishes.forEach(dish -> Objects.requireNonNull(dish, "Блюда повара не могут быть null"));
        if (cooksDishesMap.containsKey(cook)) {
            throw new IllegalArgumentException("Сотрудник: " + cook + " уже работает на кухне");
        }
        cooksDishesMap.put(cook, dishes);

    }
}
