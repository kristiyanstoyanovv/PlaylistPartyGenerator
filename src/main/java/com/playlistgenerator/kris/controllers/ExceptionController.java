package com.playlistgenerator.kris.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExceptionController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            HttpStatus httpStatus = HttpStatus.resolve(statusCode);
            if (httpStatus != null) {
                if (httpStatus.is4xxClientError()) {
                    model.addAttribute("errorCode", 400);
                    model.addAttribute("errorMessage", httpStatus);
                } else if (httpStatus.is5xxServerError()) {
                    model.addAttribute("errorCode", 500);
                    model.addAttribute("errorMessage", httpStatus);
                } else {
                    model.addAttribute("errorCode", 0);
                }
            }
        }
        return "error-page";
    }

}
