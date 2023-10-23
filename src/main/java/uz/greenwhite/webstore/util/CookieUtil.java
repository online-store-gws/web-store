package uz.greenwhite.webstore.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class CookieUtil {

    private static PasswordEncoder encoder;

    private static final String sessionTokenName = "session_token";

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        CookieUtil.encoder = encoder;
    }

    public static String getSessionCookie(HttpServletRequest request, HttpServletResponse response) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(sessionTokenName)) {
                return cookie.getValue();
            }
        }
        return setSessionCookie(response);
    }

    private static String setSessionCookie(HttpServletResponse response) {
        String token = generateToken();
        Cookie cookie = new Cookie(sessionTokenName, token);
        cookie.setPath("/");
        response.addCookie(cookie);

        return token;
    }

    private static String generateToken() {
        return encoder.encode(String.valueOf(Date.from(Instant.now()).getTime()));
    }
}
