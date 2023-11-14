package org.crock.java.winterschool.filter;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BlackListFilterTest {
    private boolean isHasBlackWord(String comment) {
        Set<String> blackList = new HashSet<>();
        blackList.add("dirty");
        if (comment == null) {
            return true;
        }
        comment = comment.toLowerCase();
        List<String> wordsOfComment = Arrays.stream(comment.split("\\P{L}+")).toList();
        return blackList.stream().anyMatch(word -> wordsOfComment.contains(word.toLowerCase()));
    }

    @Test
    public void testEqualWord() {
        List<String> comments = new ArrayList<>(Arrays.asList("here i have clear text", "Here is dirty text",
                "Here is diRTy text with different register", "....DIRTY,,,, text with some signs",
                "                                                                                ",
                "Here///is? strange....Text.Without<BaD.WORDs =)",
                "This comment has D1rtY text, with 1 mistake",
                "Clear text with '\\n',\n it is correct",
                "Another exmpl,\nDirty\n ",
                "here has a empty symb like DiRt",
                "it is  lear text", "", "..", "irtY words",
                null));
        List<String> result = new Filter().filterComments(comments, this::isHasBlackWord);

        // ќжидаетс€ точное совпадение
        ArrayList<String> expected = new ArrayList<>(Arrays.asList("here i have clear text",
                "                                                                                ",
                "Here///is? strange....Text.Without<BaD.WORDs =)",
                "This comment has D1rtY text, with 1 mistake",
                "Clear text with '\\n',\n it is correct",
                "here has a empty symb like DiRt",
                "it is  lear text",
                "",
                "..",
                "irtY words"));

        assertEquals(expected.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), expected.get(i));
        }
    }

    @Test
    void checkWithMistakes() {

        List<String> comments1 = new ArrayList<>(Arrays.asList(
                "here i have clear text", "Here is dirty text",
                "Here is diRTy text with different register", "....DIRTY,,,, text with some signs",
                "                                                                                ",
                "Here///is? strange....Text.Without<BaD.WORDs =)",
                "This comment has D1rtY text, with 1 mistake",
                "Clear text with '\\n',\n it is correct",
                "Another exmpl,\nDirty\n ",
                "here has a empty symb like DiRt",
                "it is  lear text", "", "..", "irtY words",
                null));
        List<String> result1 = new Filter().filterComments(comments1, this::withOneMistake);

        // ќжидаетс€ точное совпадение
        ArrayList<String> expected1 = new ArrayList<>(Arrays.asList("here i have clear text",
                "                                                                                ",
                "Here///is? strange....Text.Without<BaD.WORDs =)",
                "Clear text with '\\n',\n it is correct",
                "it is  lear text",
                "",
                ".."));
        for (var el : result1) {
            System.out.println(el);
        }
        assertEquals(expected1.size(), result1.size());
        for (int i = 0; i < result1.size(); i++) {
            assertEquals(expected1.get(i), result1.get(i));
        }
    }

    public boolean withOneMistake(String comment) {
        String blacKWord = "dirty";
        if (comment == null) {
            return true;
        }
        comment = comment.toLowerCase();
        List<String> wordsOfComment = Arrays.stream(comment.split("[\\s\\p{Punct}\\p{Cntrl}]+")).toList();
        for (var word : wordsOfComment) {
            if (levenshteinDistance(word, blacKWord) <= 1) {
                return true;
            }
        }
        return false;
    }

    private int levenshteinDistance(String word1, String word2) {

        int[][] dp = new int[word1.length() + 1][word2.length() + 1];

        for (int i = 0; i <= word1.length(); i++) {
            for (int j = 0; j <= word2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int min = Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                    dp[i][j] = 1 + min;
                }
            }
        }
        return dp[word1.length()][word2.length()];
    }

}