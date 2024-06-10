package org.example.servlet;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.regex.Pattern;

@WebFilter(value = "/time", initParams =  {
    @WebInitParam(name= "pattern", value= "^(?:[a-zA-Z]+/[a-zA-Z_]+|UTC[+-][0-9]{1,2})$")
})
public class TimezoneValidateFilter extends HttpFilter {
    private Pattern pattern;

    @Override
    public void init(FilterConfig config) {
        String regex = config.getInitParameter("pattern");
        pattern = Pattern.compile(regex);
    }

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {
        String timezone = req.getParameter("timezone");

        if (timezone != null) {
            timezone = timezone.replace(" ", "+");

            if (!pattern.matcher(timezone).matches()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("text/html; charset=utf-8");
                resp.getWriter().write("<html><body><h1>Invalid timezone: " + timezone +
                        "</h1><p>Enter the timezone e.g., Europe/Kiev</p>" +
                        "<button onclick=\"window.history.back();\">Back</button></body></html>");
                return;
            }
        }
        chain.doFilter(req, resp);
    }
}
