package com.feedback;

import com.feedback.model.Feedback;
import com.feedback.storage.FeedbackStore;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeedbackStoreTest {

    @Test
    void addingFeedbackIncreasesCount() {
        FeedbackStore store = FeedbackStore.getInstance();
        int before = store.count();

        store.add(new Feedback("Asha", "asha@example.com", "Great mentoring support", 5, LocalDate.now()));

        assertEquals(before + 1, store.count());
    }

    @Test
    void storedFeedbackRetainsFields() {
        FeedbackStore store = FeedbackStore.getInstance();
        store.add(new Feedback("Ravi", "ravi@example.com", "Needs faster responses", 3, LocalDate.now()));

        boolean found = store.getAll().stream()
                .anyMatch(f -> f.getEmail().equals("ravi@example.com") && f.getRating() == 3);

        assertTrue(found, "Expected to find the feedback we just added");
    }
}
