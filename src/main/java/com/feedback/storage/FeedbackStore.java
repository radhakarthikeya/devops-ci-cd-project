package com.feedback.storage;

import com.feedback.model.Feedback;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Simple in-memory store for demo purposes.
 * Data resets whenever the app is redeployed/restarted -
 * this is intentional for a learning project (no DB required).
 */
public class FeedbackStore {

    private static final FeedbackStore INSTANCE = new FeedbackStore();
    private final List<Feedback> feedbackList = new CopyOnWriteArrayList<>();

    private FeedbackStore() {}

    public static FeedbackStore getInstance() {
        return INSTANCE;
    }

    public void add(Feedback feedback) {
        feedbackList.add(feedback);
    }

    public List<Feedback> getAll() {
        return Collections.unmodifiableList(feedbackList);
    }

    public int count() {
        return feedbackList.size();
    }
}
