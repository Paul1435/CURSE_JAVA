package org.crock.winterschool.kitchen;

import java.util.*;

public class Dish implements Comparable<Dish> {
    private int kingRating;
    private String dishName;
    private double weight;
    private int courtiersRating;

    private Set<String> ingredients;
    private Category category;

    @Override
    public int compareTo(Dish anotherDish) {
        int diff = Integer.compare(anotherDish.kingRating, kingRating);
        if (diff == 0) {
            return Integer.compare(anotherDish.courtiersRating, courtiersRating);
        }
        return diff;
    }

    public Set<String> getIngredients() {
        return new HashSet<>(ingredients);
    }

    public enum Category {
        SNACK,
        HOT,
        DESSERT
    }

    @Override
    public String toString() {
        return dishName + ":\n" +
                "kingRating= " + kingRating + '\n' +
                "courtiersRating= " + courtiersRating + '\n' +
                "weight= " + weight + '\n' +
                "ingredients= " + ingredients + '\n' +
                "category= " + category + '\n';
    }

    public int getCourtiersRating() {
        return courtiersRating;
    }

    public void setCourtiersRating(int courtiersRating) {
        if (courtiersRating < 0 || courtiersRating > 100) {
            throw new IllegalArgumentException("Невозможен рейтинг " + courtiersRating + " у придворных");
        }
        this.courtiersRating = courtiersRating;
    }

    public int getKingRating() {
        return kingRating;
    }

    public void setKingRating(int kingRating) {
        if (kingRating < 0 || kingRating > 100) {
            throw new IllegalArgumentException("Невозможен рейтинг " + kingRating + " у короля");
        }
        this.kingRating = kingRating;
    }

    public Dish(String dishName, Collection<String> ingredients, Category category, int kingRating, int courtiersRating, double weight) {
        Objects.requireNonNull(dishName, "Блюдо не может быть без имени");
        this.dishName = dishName;
        if (ingredients == null || ingredients.size() == 0) {
            throw new IllegalArgumentException("У блюда должны быть ингредиенты");
        }
        ingredients.forEach(ingredient -> Objects.requireNonNull(ingredient, "Ингредиенты не могут быть null"));
        this.ingredients = new HashSet<>(ingredients);
        Objects.requireNonNull(category, "Не указан тип блюда");
        this.category = category;

        setCourtiersRating(courtiersRating);
        setKingRating(kingRating);
        if (weight <= 0) {
            throw new IllegalArgumentException("Масса не может быть = " + weight);
        }
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Double.compare(dish.weight, weight) == 0 && (dish.dishName.equalsIgnoreCase(dishName)) && (dish.ingredients.equals(ingredients)) && category.equals(dish.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dishName, weight, ingredients, category);
    }

    public static Set<String> generateIngredients(String... ingredients) {
        return new HashSet<>(Arrays.asList(ingredients));
    }
}
