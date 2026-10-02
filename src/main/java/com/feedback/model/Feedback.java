package com.feedback.model;

import java.time.LocalDate;

public class Feedback {
    private final String name;
    private final String email;
    private final String message;
    private final int rating;
    private final LocalDate date;

    public Feedback(String name, String email, String message, int rating, LocalDate date) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.rating = rating;
        this.date = date;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMessage() { return message; }
    public int getRating() { return rating; }
    public LocalDate getDate() { return date; }
}
