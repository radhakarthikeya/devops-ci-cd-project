package com.feedback;

import com.feedback.model.Feedback;
import com.feedback.storage.FeedbackStore;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/submit")
public class FeedbackServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String message = req.getParameter("message");
        String ratingParam = req.getParameter("rating");

        int rating = 5;
        try {
            if (ratingParam != null && !ratingParam.isBlank()) {
                rating = Integer.parseInt(ratingParam);
            }
        } catch (NumberFormatException ignored) {
            // fall back to default rating
        }

        if (name == null || name.isBlank() || email == null || email.isBlank()) {
            req.setAttribute("error", "Name and email are required.");
            req.getRequestDispatcher("/feedback.jsp").forward(req, resp);
            return;
        }

        Feedback feedback = new Feedback(name.trim(), email.trim(),
                message == null ? "" : message.trim(), rating, LocalDate.now());
        FeedbackStore.getInstance().add(feedback);

        resp.sendRedirect(req.getContextPath() + "/success.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Redirect stray GETs back to the form
        resp.sendRedirect(req.getContextPath() + "/feedback.jsp");
    }
}
