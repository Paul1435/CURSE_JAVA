package org.crock.winterschool.filter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
            String[] wordsOfComment = comments.get(i).split("\\P{L}+");
            for (var word : wordsOfComment) {
                for (var blackWord : blackList) {
                    boolean needToMask = false;
                    if (word.length() == blackWord.length()) {
                        needToMask = SimilarWords.isSimilarWordsWithEqualLen(word, blackWord);
                    } else if (Math.abs(word.length() - blackWord.length()) <= 1) {
                        needToMask = SimilarWords.isSimilarWordsWithDifferentLen(word, blackWord);
                    }
                    if (needToMask) {
                        String mask = "*".repeat(word.length());
                        comments.set(i, comments.get(i).replace(word, mask));
                        break;
                    }
                }
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
            String[] wordsOfComment = comments.get(i).split("\\P{L}+");
            for (var word : wordsOfComment) {
                for (var blackWord : blackList) {
                    int distance = levenshteinDistance(word.toLowerCase(), blackWord.toLowerCase());
                    if (distance <= 1) {
                        String mask = "*".repeat(word.length());
                        comments.set(i, comments.get(i).replace(word, mask));
                        break;
                    }
                }
            }
        }
    }

}
