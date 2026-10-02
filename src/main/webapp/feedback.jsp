<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Submit Feedback</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 480px; margin: 60px auto; color: #222; }
        label { display: block; margin-top: 14px; font-weight: bold; }
        input, textarea, select { width: 100%; padding: 8px; margin-top: 4px; box-sizing: border-box; }
        button { margin-top: 20px; padding: 10px 20px; background: #2c7be5; color: #fff;
                 border: none; border-radius: 4px; cursor: pointer; }
        .error { color: #c0392b; margin-top: 14px; }
        a { color: #2c7be5; }
    </style>
</head>
<body>
    <h1>Submit Feedback</h1>
    <% if (request.getAttribute("error") != null) { %>
        <p class="error"><%= request.getAttribute("error") %></p>
    <% } %>
    <form method="post" action="submit">
        <label for="name">Name</label>
        <input type="text" id="name" name="name" required>

        <label for="email">Email</label>
        <input type="email" id="email" name="email" required>

        <label for="rating">Rating (1-5)</label>
        <select id="rating" name="rating">
            <option value="5">5 - Excellent</option>
            <option value="4">4 - Good</option>
            <option value="3">3 - Average</option>
            <option value="2">2 - Poor</option>
            <option value="1">1 - Very poor</option>
        </select>

        <label for="message">Message / status</label>
        <textarea id="message" name="message" rows="4"></textarea>

        <button type="submit">Submit</button>
    </form>
    <p><a href="index.jsp">&larr; Back to home</a></p>
</body>
</html>
