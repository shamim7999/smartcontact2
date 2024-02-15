package com.spring.boot.smartcontact.controller.errors;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CentralExceptionHandler {

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public String exceptionHandler1(Model model) {
        model.addAttribute("msg", "ERRrrr....");
        return "null_page";
    }

    @ExceptionHandler({IndexOutOfBoundsException.class})
    public String exceptionHandler2(Model model) {
        model.addAttribute("msg", "ERRrrr....");
        return "null_page";
    }

    @ExceptionHandler({NullPointerException.class, NumberFormatException.class})
    public String exceptionHandler() {
        return "null_page";
    }
}