package com.elice.boardproject.global.error;

import com.elice.boardproject.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 커스텀 에러 컨트롤러 클래스입니다.
 * 애플리케이션에서 발생한 오류를 처리하고, 에러 페이지를 반환합니다.
 */
@Slf4j
@Controller
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    /**
     * 오류 발생 시 호출되는 메서드입니다.
     * 오류 발생 시 상태 코드를 확인하고, 해당 상태 코드에 대한 에러 메시지를 로그로 기록한 후,
     * 에러 페이지로 리다이렉트합니다.
     *
     * @param request HTTP 요청 객체
     * @return 에러 페이지 경로
     */
    @RequestMapping(value = PATH)
    public String error(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        if (statusCode != null) {
            log.error("Error occurred with status code: {}", statusCode);
        } else {
            log.error("An unknown error occurred");
        }

        return "global/error";
    }
}
