package org.crock.winterschool;

import org.crock.winterschool.filter.Filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Filter filter = new Filter();
        // Тест с удалением
        List<String> comments = new ArrayList<>(3);
        comments.add("Ночь была нежна, скоро в смуте появится день");
        comments.add("Книга \"бог\", как  может исправить человека");
        comments.add("А вас,Молодой человек, я попрошу остаться");
        // Не удалится, так как это два разных слова
        comments.add("А вас,Молодой человека, я попрошу остаться");
        comments.add("Тут написан......ПЛОХОЙ. текст, он должен пропасть");

        HashSet<String> blackList = new HashSet<>();
        blackList.add("человек");
        blackList.add("Бог");
        blackList.add("плохой");

        filter.filterComments(comments, blackList);
        comments.forEach(System.out::println);
        System.out.println();
        comments.clear();

        // Проверка маскировки сообщения через динамическое программирование
        comments.clear();
        comments.add("Ночь была нежна");
        comments.add("Книга \"богемная рапсодия\", как Бог, может исправить человека");
        comments.add("А вас,Молодой человек, я попрошу остаться");
        comments.add("А вас,Молодой челJвек, я попрошу остаться.");
        comments.add("Тут написан......ПЛОХОЙ. текст, он должен пропасть...");

        blackList = new HashSet<>();
        blackList.add("человек");
        blackList.add("Бог");
        blackList.add("плохой");

        filter.censorshipWordsFastAlgorithm(comments, blackList);
        comments.forEach(System.out::println);
        comments.clear();
        System.out.println();

        //Проверка второго алгоритма
        comments.clear();
        comments.add("Ночь была нежна, скоро в смуте появится день");
        comments.add("Книга \"богемная рапсодия\", как бог, может исправить человека");
        comments.add("А вас,Молодой человек, я попрошу остаться");
        comments.add("А вас,Молодой человека, я попрошу остаться");
        comments.add("Тут написан......ПЛОХОЙ. текст, он должен пропасть");

        blackList = new HashSet<>();
        blackList.add("человек");
        blackList.add("Бог");
        blackList.add("плохой");

        filter.censorshipWordsBrutForce(comments, blackList);
        comments.forEach(System.out::println);
        comments.clear();
        System.out.println();

    }

}