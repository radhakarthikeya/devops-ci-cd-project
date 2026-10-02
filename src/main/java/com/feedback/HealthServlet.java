package com.feedback;

import com.feedback.storage.FeedbackStore;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Used by the Jenkins pipeline's "Verify" stage:
 *   curl -f http://<host>:<port>/student-feedback-portal/health
 * Returns HTTP 200 + "OK" when the app deployed and started correctly.
 */
@WebServlet("/health")
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        resp.getWriter().println("FAIL - store unavailable: " + FeedbackStore.getInstance().count());
    }
}
