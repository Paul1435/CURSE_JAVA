package org.crock.java.winterschool.filter;

interface SimilarWords {
    static boolean isSimilarWordsWithDifferentLen(String word1, String word2) {
        int countMistake = 0;
        int indexWord1 = 0;
        int indexWord2 = 0;
        if (!(word1.length() - word2.length() > 0)) {
            String temp = word1;
            word1 = word2;
            word2 = temp;
        }
        String lowerCaseWord1 = word1.toLowerCase();
        String lowerCaseWord2 = word2.toLowerCase();

        while (indexWord1 < word1.length() && indexWord2 < word2.length()) {
            if (lowerCaseWord1.charAt(indexWord1) == lowerCaseWord2.charAt(indexWord2)) {
                ++indexWord1;
                ++indexWord2;
            } else {
                countMistake++;
                if (countMistake >= 2) {
                    break;
                }
                if (lowerCaseWord1.charAt(indexWord1 + 1) == lowerCaseWord2.charAt(indexWord2)) {
                    ++indexWord1;
                } else {
                    countMistake++;
                    break;
                }
            }
        }
        return countMistake < 2;
    }

    static boolean isSimilarWordsWithEqualLen(final String word1, final String word2) {
        int countMist = 0;
        String lowerCaseWord1 = word1.toLowerCase();
        String lowerCaseWord2 = word2.toLowerCase();
        for (int i = 0; i < word1.length(); i++) {
            countMist += lowerCaseWord1.charAt(i) == lowerCaseWord2.charAt(i) ? 0 : 1;
            if (countMist >= 2) {
                break;
            }
        }
        return countMist < 2;
    }
}
