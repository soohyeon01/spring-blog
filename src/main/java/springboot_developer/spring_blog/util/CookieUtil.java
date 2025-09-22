package springboot_developer.spring_blog.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.util.Base64;

/**
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

    // 객체를 직렬화해 쿠키의 값으로 변환
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    // 쿠키를 역직렬화해 객체로 변환
    /**
     *  보안상의 문제로 "deserialize(byte[])' is deprecated" 되어
     *  Jackson 라이브러리를 사용하여 객체로 변환하는 방법 선택
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T deserialize(Cookie cookie, Class<T> cls) throws IOException {
        return objectMapper.readValue(
                Base64.getUrlDecoder().decode(cookie.getValue()), cls
        );
    }
}
