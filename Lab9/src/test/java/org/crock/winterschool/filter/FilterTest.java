package org.crock.winterschool.filter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class FilterTest {
    Filter filter = new Filter();

    @Test
    void checkNullAndEmptyObjects() {
        // filter

        assertDoesNotThrow(() -> filter.filterComments(null, null));
        assertDoesNotThrow(() -> filter.filterComments(new ArrayList<>(), null));
        assertDoesNotThrow(() -> filter.filterComments(null, new HashSet<>()));

        //Masking
        assertDoesNotThrow(() -> filter.censorshipWordsFastAlgorithm(null, null));
        assertDoesNotThrow(() -> filter.censorshipWordsFastAlgorithm(new ArrayList<>(), null));
        assertDoesNotThrow(() -> filter.censorshipWordsFastAlgorithm(null, new HashSet<>()));

        assertDoesNotThrow(() -> filter.censorshipWordsBrutForce(null, null));
        assertDoesNotThrow(() -> filter.censorshipWordsBrutForce(new ArrayList<>(), null));
        assertDoesNotThrow(() -> filter.censorshipWordsBrutForce(null, new HashSet<>()));
    }

    @Test
    void checkDifferentRegistersFilter() {
        ArrayList<String> comments = new ArrayList<>();
        ArrayList<String> expectedResult = new ArrayList<>();
        HashSet<String> blackWords = new HashSet<>();
        blackWords.add("God");
        blackWords.add("Venum");
        blackWords.add("bad");
        blackWords.add("Python");
        blackWords.add("vIm");
        comments.add("Goddemian ///");
        comments.add("God, send  on  Earth.");
        comments.add("Gode, send him on  Earth.");
        comments.add("Venum,come to me...");
        comments.add("java - bAd lang");
        comments.add("you-Bad...person");
        comments.add("Python the best lang");
        comments.add("Pythonnot good  lang");
        comments.add("vims not actual ide");

        //here substring of god
        expectedResult.add("Goddemian ///");
        expectedResult.add("Gode, send him on  Earth.");
        //here substring of python
        expectedResult.add("Pythonnot good  lang");
        // here subs of vim
        expectedResult.add("vims not actual ide");
        filter.filterComments(comments, blackWords);
        assertEquals(expectedResult, comments);
    }

    @Test
    void checkDifferentRegistersCensorshipWords() {
        ArrayList<String> comments1 = new ArrayList<>();
        comments1.add("Bookmaker your friend");
        comments1.add("victure....... will help me");
        comments1.add("victum,,,,, help me");
        comments1.add("-book. Can you take care of me>?");
        ArrayList<String> comments2 = new ArrayList<>(comments1);
        ArrayList<String> expectedResult = new ArrayList<>();
        expectedResult.add("Bookmaker your friend");
        expectedResult.add("*******....... will help me");
        expectedResult.add("victum,,,,, help me");
        expectedResult.add("-****. Can you take care of me>?");

        HashSet<String> blackWords = new HashSet<>();
        blackWords.add("victUre");
        blackWords.add("Book");
        filter.censorshipWordsBrutForce(comments1, blackWords);
        filter.censorshipWordsFastAlgorithm(comments2, blackWords);
        //expect the same result from two identical data, in two different algorithms
        assertEquals(comments1, comments2);
        // expect right Results
        assertEquals(comments1, expectedResult);
    }

    @Test
    void checkOneMistakeInMaskingMethod() {
        ArrayList<String> comments1 = new ArrayList<>();
        comments1.add("Goddemian resource");
        comments1.add("Bookmaker your friend");
        comments1.add("victure....... will help me");
        comments1.add("Gode,,,,, help me");
        comments1.add("-book. Can you take care of me>?");

        ArrayList<String> comments2 = new ArrayList<>(comments1);

        ArrayList<String> expectedResult = new ArrayList<>();
        expectedResult.add("Goddemian resource");
        expectedResult.add("********* your friend");
        expectedResult.add("*******....... will help me");
        expectedResult.add("****,,,,, help me");
        expectedResult.add("-book. Can you take care of me>?");

        HashSet<String> blackWords = new HashSet<>();
        blackWords.add("victUre");
        blackWords.add("God");
        blackWords.add("Bookmake");

        filter.censorshipWordsBrutForce(comments1, blackWords);
        filter.censorshipWordsFastAlgorithm(comments2, blackWords);
        //expect the same result from two identical data, in two different algorithms
        assertEquals(comments1, comments2);
        // expect right Results
        assertEquals(comments1, expectedResult);
    }

}