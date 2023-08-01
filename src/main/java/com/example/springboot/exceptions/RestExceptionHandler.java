package com.example.springboot.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler     {


    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    //other exception handlers below

    //Not found exception
    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            NoSuchElementException ex) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        //Api Error
        ApiError apiError = new ApiError(BAD_REQUEST);

        apiError.setMessage("Arguments are not valid");

        //Define a list of subErrors
        List<ApiValidationError> apiValidationErrorList = new ArrayList<>();

        //for each error extract the field name and the error message and use it to build the ApiSubError object
        ex.getBindingResult().getAllErrors().forEach( error ->{
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();

                    ApiValidationError apiValidationError = new ApiValidationError(fieldName,errorMessage);
                    apiValidationErrorList.add(apiValidationError);
                }
        );
        //set the error list of sub errors
        apiError.setSubErrors(apiValidationErrorList);
        return buildResponseEntity(apiError);
    }
}