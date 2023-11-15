package org.example;

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
    }
}