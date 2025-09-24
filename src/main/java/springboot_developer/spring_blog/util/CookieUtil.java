package springboot_developer.spring_blog.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

/**
 * 쿠키가 필요할 때 마다 생성하고 삭제하는 로직을 추가하면 불편하므로
 * 유틸리티로 사용할 쿠키 관리 클래스 구현
 */
public class CookieUtil {
    // 요청값(이름, 값, 만료 기간)을 바탕으로 쿠키 추가
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    // 쿠키의 이름을 입력받아 쿠키 삭제(실제로 삭제하는 법이 없으므로 파라미터로 넘어온 키의 쿠키 값을 빈 값으로 바꾸고 만료시간을 0으로 설정)
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        if (cookie == null || cookie.getValue() == null) return null;

        byte[] data = Base64.getUrlDecoder().decode(cookie.getValue());
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            Object obj = ois.readObject();
            return cls.cast(obj);
        } catch (IOException | ClassNotFoundException e) {
            // 연습용이라 간단히 RuntimeException으로 처리했지만,
            // 실무에서는 로그 남기고 null 반환하거나 쿠키 만료 처리하는 편이 안전
            throw new RuntimeException("Failed to deserialize cookie", e);
        }
    }
}
