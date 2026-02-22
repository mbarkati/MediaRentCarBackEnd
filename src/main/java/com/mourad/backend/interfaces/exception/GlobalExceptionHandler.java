package com.mourad.backend.interfaces.exception;

import com.mourad.backend.domain.exception.CarAlreadyExistsException;
import com.mourad.backend.domain.exception.CarNotFoundException;
import com.mourad.backend.domain.exception.InvalidCarStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CarNotFoundException.class)
    public ProblemDetail handleCarNotFound(CarNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Car Not Found");
        problem.setType(URI.create("https://api.car-rental.com/errors/car-not-found"));
        return problem;
    }

    @ExceptionHandler(CarAlreadyExistsException.class)
    public ProblemDetail handleCarAlreadyExists(CarAlreadyExistsException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Car Already Exists");
        problem.setType(URI.create("https://api.car-rental.com/errors/car-already-exists"));
        return problem;
    }

    @ExceptionHandler(InvalidCarStateException.class)
    public ProblemDetail handleInvalidCarState(InvalidCarStateException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problem.setTitle("Invalid Car State");
        problem.setType(URI.create("https://api.car-rental.com/errors/invalid-car-state"));
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors);
        problem.setTitle("Validation Failed");
        problem.setType(URI.create("https://api.car-rental.com/errors/validation-failed"));
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problem.setTitle("Internal Server Error");
        problem.setType(URI.create("https://api.car-rental.com/errors/internal-error"));
        return problem;
    }
}
