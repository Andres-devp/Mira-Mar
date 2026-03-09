package com.example.demo.handler;

import com.example.demo.exception.NotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException ex, Model model) {
        String mensaje = ex.getMessage();
        if (ex.getId() != null) {
            mensaje += " (ID: " + ex.getId() + ")";
        }
        model.addAttribute("mensaje", mensaje);
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("mensaje", "Ocurrió un error: " + ex.getMessage());
        return "error";
    }
}
