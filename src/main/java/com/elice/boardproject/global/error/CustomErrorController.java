package com.elice.boardproject.global.error;

import com.elice.boardproject.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";
    private final UserService userService;

    public CustomErrorController(UserService userService) {
        this.userService = userService;
    }

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
