package com.example.productservice.ControllerAdvice;

import com.example.productservice.DTOs.ExceptionDTO;
import com.example.productservice.Exceptions.InvalidTokenException;
import com.example.productservice.Exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    private ResponseEntity<ExceptionDTO> handleProductNotFoundException(
            ProductNotFoundException productNotFoundException){

        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setStatus("FAILURE");
        exceptionDTO.setMessage(productNotFoundException.getMessage());

        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTokenException.class)
    private ResponseEntity<ExceptionDTO> handleInvalidTokenException(
            InvalidTokenException invalidTokenException){

        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setStatus("FAILURE");
        exceptionDTO.setMessage(invalidTokenException.getMessage());

        return new ResponseEntity<>(exceptionDTO, HttpStatus.UNAUTHORIZED);
    }
}
