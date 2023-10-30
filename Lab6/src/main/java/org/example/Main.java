package org.example;

import org.example.annotation.AnnotatedImage;
import org.example.annotation.Annotation;
import org.example.figure.Circle;
import org.example.figure.Point;
import org.example.figure.Rectangle;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Annotation[] annotations = new Annotation[]{
                new Annotation("signature", new Circle(new Point(), 5)),
                new Annotation("signature1", new Rectangle(new Point(), new Point(5, 5))),
                new Annotation("signature2", new Circle(new Point(3.0, 4.5), 3))};
        AnnotatedImage test = new AnnotatedImage("test", annotations);

        Arrays.stream(annotations).forEach(annotation -> System.out.println(annotation.toString()));
        Annotation testFindPoint = test.findByPoint(-5, 0);
        System.out.println(testFindPoint);

        testFindPoint.getFigure().move(3, 3);
        System.out.println(testFindPoint);


    }
}