package org.crock.winterschool;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Main {
    public static <T, U> Function<T, U> ternaryOperator(
            Predicate<? super T> condition,
            Function<? super T, ? extends U> ifTrue,
            Function<? super T, ? extends U> ifFalse) {
        return t -> condition.test(t) ? ifTrue.apply(t) : ifFalse.apply(t);
    }

    public static void main(String[] args) {
        Predicate<Integer> isEven = x -> x % 2 == 0;

        Function<Integer, String> ifTrueFunction = x -> ("Even");
        Function<Integer, String> ifFalseFunction = x -> ("Odd");

        Function<Integer, String> resultFunction = ternaryOperator(isEven, ifTrueFunction, ifFalseFunction);
        // Example usage:
        System.out.println(resultFunction.apply(4));  // Output: Even
        System.out.println(resultFunction.apply(3));  // Output: Odd
        //UPPER and lower
        Function<String, String> result1 = ternaryOperator(
                s -> s.length() > 5,
                s -> s.toUpperCase(),
                s -> s.toLowerCase()
        );
        System.out.println(result1.apply("all in lower case")); // Вывод: ALL IN LOWER CASE
        System.out.println(result1.apply("HELL"));  // Вывод: hell


        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        Function<String, String> result2 = ternaryOperator(
                names::contains,
                s -> s + " в листе",
                s -> s + " не в листе"
        );
        System.out.println(result2.apply("Я"));    // Вывод: Я не в листе
        System.out.println(result2.apply("Bob"));  // Вывод: Bob в листе


    }
}