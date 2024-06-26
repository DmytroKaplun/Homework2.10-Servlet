package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String timezone = req.getParameter("timezone");
        ZoneId zoneId;

        zoneId = getZoneId(resp, timezone);
        if (zoneId == null) return;
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'XXX");
            String formattedDateTime = zonedDateTime.format(formatter);
            resp.setContentType("text/html; charset=utf-8");
        try {
            resp.getWriter().write("<html><body><h1>" + formattedDateTime + "</h1></body></html>");
            resp.getWriter().close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    private static ZoneId getZoneId(HttpServletResponse resp, String timezone) {
        ZoneId zoneId;
        if (timezone != null && !timezone.trim().isEmpty()) {
            timezone = timezone.replace(" ", "+");
            try {
                zoneId = ZoneId.of(timezone);
            } catch (DateTimeException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("text/html; charset=utf-8");
                try {
                    resp.getWriter().write("<html><body><h1>Invalid timezone: " + timezone +
                            "</h1><p>Enter the timezone e.g., Europe/Kiev</p>" +
                            "<button onclick=\"window.history.back();\">Back</button></body></html>");
                } catch (IOException ex) {
                    e.getStackTrace();
                }
                return null;
            }
        } else {
            zoneId = ZoneId.of("UTC");
        }
        return zoneId;
    }
}

