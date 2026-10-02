<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Feedback Portal</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 640px; margin: 60px auto; color: #222; }
        h1 { color: #2c3e50; }
        a.button { display: inline-block; margin-top: 20px; padding: 10px 20px;
                   background: #2c7be5; color: #fff; text-decoration: none; border-radius: 4px; }
        .meta { color: #777; font-size: 0.9em; margin-top: 40px; }
    </style>
</head>
<body>
    <h1>Student Feedback Portal</h1>
    <p>A small Java web app used as the demo target for a Jenkins CI/CD pipeline
       (Git &rarr; Maven build/test &rarr; package WAR &rarr; deploy to Tomcat &rarr; health check).</p>
    <a class="button" href="feedback.jsp">Submit Feedback</a>
    <p class="meta">Health endpoint: <a href="health">/health</a></p>
</body>
</html>
