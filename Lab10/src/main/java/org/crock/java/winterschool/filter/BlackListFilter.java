package org.crock.java.winterschool.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public interface BlackListFilter {

    /**
     * From the given list of comments removes ones
     * that contain words from the black list.
     *
     * @param comments  list of comments; every comment
     *                  is a sequence of words, separated
     *                  by spaces, punctuation or line breaks
     * @param blackList list of words that should not
     *                  be present in a comment
     */
    void filterComments(List<String> comments, Set<String> blackList);

    default <T> ArrayList<T> filterComments(Iterable<T> comments, Predicate<T> blackList) {
        return new ArrayList<>(StreamSupport.stream(comments.spliterator(), false)
                .filter(blackList.negate())
                .toList());
    }
}
