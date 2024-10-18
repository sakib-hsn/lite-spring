package com.sakib.io.service;

import com.sakib.io.litespring.annotation.Autowired;
import com.sakib.io.litespring.annotation.Component;
import com.sakib.io.litespring.context.UserContext;
import com.sakib.io.models.SessionData;
import com.sakib.io.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomSessionService {
    private Map<String, SessionData> sessionDataMap = new HashMap<>();
    private static final String CUSTOM_SESSION_ID = "CUSTOM_SESSION_ID";

    @Autowired
    private UserRepository userRepository;

    public SessionData createSession(String sessionId, String username, HttpServletResponse response) {
        if (sessionDataMap.containsKey(sessionId)) return null;
        SessionData sessionData = SessionData.builder()
                .id(sessionId)
                .username(username)
                .createdTime(System.currentTimeMillis())
                .lastLoginTime(System.currentTimeMillis())
                .build();
        sessionDataMap.put(sessionId, sessionData);

//        Cookie cookie = new Cookie(CUSTOM_SESSION_ID, sessionId);
//        cookie.setMaxAge(10000);
//        cookie.setPath("/");
//        response.addCookie(cookie);

        return sessionData;
    }

    public SessionData findSessionAndSetContext(HttpServletRequest request) {

//        if use cookie based session
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                System.out.println("cookie.getName() = " + cookie.getName());
//                System.out.println("cookie.getValue() = " + cookie.getValue());
//                if (cookie.getName().equals(CUSTOM_SESSION_ID)) {
//                    if (sessionDataMap.containsKey(cookie.getValue())) {
//                        SessionData sessionData = sessionDataMap.get(cookie.getValue());
//                        System.out.println("sessionData.getUsername() = " + sessionData.getUsername());
//                        UserContext.setUserContext(userRepository.getUser(sessionData.getUsername()));
//                        return sessionData;
//                    }
//                }
//            }
//        }
        // token based
        String token = request.getParameter("token");
        if(sessionDataMap.containsKey(token)) {
            SessionData sessionData = sessionDataMap.get(token);
            System.out.println("sessionData.getUsername() = " + sessionData.getUsername());
            UserContext.setUserContext(userRepository.getUser(sessionData.getUsername()));
            return sessionData;
        }
        return null;
    }

    public void deleteSession(String sessionId) {
        sessionDataMap.remove(sessionId);
    }
}
