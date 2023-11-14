package org.crock.java.winterschool.filter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class Filter implements BlackListFilter {
    private boolean isHasBlackWord(String comment, Set<String> blackList) {
        if (comment == null) {
            return false;
        }
        comment = comment.toLowerCase();
        List<String> wordsOfComment = Arrays.stream(comment.split("\\P{L}+")).toList();
        return blackList.stream().anyMatch(word -> wordsOfComment.contains(word.toLowerCase()));
    }

    @Override
    public void filterComments(List<String> comments, Set<String> blackList) {
        if (comments == null || blackList == null) {
            return;
        }
        comments.removeIf(comment -> isHasBlackWord(comment, blackList));
    }


    public void censorshipWordsBrutForce(List<String> comments, Set<String> blackList) {
        if (comments == null || blackList == null) {
            return;
        }
        for (int i = 0; i < comments.size(); i++) {
            comments.set(i, process(comments.get(i), blackList, this::censorIfBlackWordBrutForce));
        }
    }

    private void censorIfBlackWordBrutForce(StringBuilder tempWord, Set<String> blackList) {
        for (var blackWord : blackList) {
            boolean needToMask = false;
            if (tempWord.length() == blackWord.length()) {
                needToMask = SimilarWords.isSimilarWordsWithEqualLen(tempWord.toString(), blackWord);
            } else if (Math.abs(tempWord.length() - blackWord.length()) <= 1) {
                needToMask = SimilarWords.isSimilarWordsWithDifferentLen(tempWord.toString(), blackWord);
            }
            if (needToMask) {
                tempWord.replace(0, tempWord.length(), "*".repeat(tempWord.length()));
                break;
            }
        }
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

    public void censorshipWordsFastAlgorithm(List<String> comments, Set<String> blackList) {
        if (comments == null || blackList == null) {
            return;
        }
        for (int i = 0; i < comments.size(); i++) {
            comments.set(i, process(comments.get(i), blackList, this::censorIfBlackWord));
        }
    }

    private void censorIfBlackWord(StringBuilder word, Set<String> blackList) {
        for (var blackWord : blackList) {
            int distance = levenshteinDistance(word.toString().toLowerCase(), blackWord.toLowerCase());
            if (distance <= 1) {
                word.replace(0, word.length(), "*".repeat(word.length()));
                break;
            }
        }
    }

    private String process(String currentComment, Set<String> blackList, BiConsumer<StringBuilder, Set<String>> censorFunction) {
        StringBuilder tempComment = new StringBuilder(currentComment.length());
        StringBuilder tempWord = new StringBuilder();

        for (int j = 0; j < currentComment.length(); j++) {
            char currentChar = currentComment.charAt(j);

            if (Character.isLetterOrDigit(currentChar)) {
                tempWord.append(currentChar);
            } else {
                censorFunction.accept(tempWord, blackList);
                tempComment.append(tempWord).append(currentChar);
                tempWord.setLength(0);
            }
        }
        censorFunction.accept(tempWord, blackList);
        tempComment.append(tempWord);
        return tempComment.toString();
    }
}
