package com.example.springboot.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
//So this doesn't call the equals and hash code implementation of the super class before calling the ones implemented in this class
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {

    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    ApiValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }


}
